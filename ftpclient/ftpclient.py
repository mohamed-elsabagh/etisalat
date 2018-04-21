# coding=utf-8
import threading
import logging
import ftplib
import socket
import time
import os

class FtpUploadTracker:
    sizeWritten = 0
    totalSize = 0.0
    lastShownPercent = 0

    def __init__(self, totalSize):
        self.totalSize = totalSize

    def handle(self, block):
        self.sizeWritten += 1024
        percentComplete = round((self.sizeWritten / self.totalSize) * 100)

        if (self.lastShownPercent != percentComplete):
            self.lastShownPercent = percentComplete
            print(str(percentComplete) + "% complete ramaing: " + str(self.totalSize - self.sizeWritten), flush=True)

def setInterval(interval, times = -1):
    # This will be the actual decorator,
    # with fixed interval and times parameter
    def outer_wrap(function):
        # This will be the function to be
        # called
        def wrap(*args, **kwargs):
            stop = threading.Event()

            # This is another function to be executed
            # in a different thread to simulate setInterval
            def inner_wrap():
                i = 0
                while i != times and not stop.isSet():
                    stop.wait(interval)
                    function(*args, **kwargs)
                    i += 1

            t = threading.Timer(0, inner_wrap)
            t.daemon = True
            t.start()
            return stop
        return wrap
    return outer_wrap


class PyFTPclient:
    def __init__(self, host, port, login, passwd, upload_directory, monitor_interval = 30):
        self.host = host
        self.port = port
        self.login = login
        self.passwd = passwd
        self.upload_directory = upload_directory
        self.monitor_interval = monitor_interval
        self.ptr = None
        self.max_attempts = 15
        self.waiting = True


    def DownloadFile(self, dst_filename, local_filename = None):
        start_time = time.time()
        res = ''
        if local_filename is None:
            local_filename = dst_filename

        with open(local_filename, 'w+b') as f:
            self.ptr = f.tell()

            @setInterval(self.monitor_interval)
            def monitor():
                if not self.waiting:
                    i = f.tell()
                    if self.ptr < i:
                        logging.debug("%d  -  %0.1f Kb/s" % (i, (i-self.ptr)/(1024*self.monitor_interval)))
                        self.ptr = i
                    else:
                        ftp.close()


            def connect():
                ftp.connect(self.host, self.port)
                ftp.login(self.login, self.passwd)
                # optimize socket params for download task
                ftp.sock.setsockopt(socket.SOL_SOCKET, socket.SO_KEEPALIVE, 1)
                ftp.sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_KEEPINTVL, 75)
                ftp.sock.setsockopt(socket.IPPROTO_TCP, socket.TCP_KEEPIDLE, 60)

            ftp = ftplib.FTP()
            ftp.set_debuglevel(2)
            ftp.set_pasv(True)

            connect()
            ftp.voidcmd('TYPE I')
            dst_filesize = ftp.size(dst_filename)

            mon = monitor()
            while dst_filesize > f.tell():
                try:
                    connect()
                    self.waiting = False
                    # retrieve file from position where we were disconnected
                    res = ftp.retrbinary('RETR %s' % dst_filename, f.write) if f.tell() == 0 else \
                              ftp.retrbinary('RETR %s' % dst_filename, f.write, rest=f.tell())

                except:
                    self.max_attempts -= 1
                    if self.max_attempts == 0:
                        mon.set()
                        logging.exception('')
                        raise
                    self.waiting = True
                    logging.info('waiting 30 sec...')
                    time.sleep(30)
                    logging.info('reconnect')


            mon.set() #stop monitor
            ftp.close()

            if not res.startswith('226 Transfer complete'):
                logging.error('Downloaded file {0} is not full.'.format(dst_filename))
                # os.remove(local_filename)
                return None


            end_time = time.time()
            print("--- filename = %s ---" % (dst_filename))
            print("--- %s attempts ---" % (16 - self.max_attempts))
            print("--- %s seconds ---" % (end_time - start_time))

            return 1

    def UploadFile(self, filename):
        tries = 0
        done = False
        start_time = time.time()
        print("Uploading " + str(filename) + " to " + str(self.upload_directory), flush=True)
        while tries < 50 and not done:
            try:
                tries += 1
                with ftplib.FTP(self.host) as ftp:
                    ftp.set_debuglevel(2)
                    print("login", flush=True)
                    ftp.login(self.login, self.passwd)
                    # ftp.set_pasv(False)
                    ftp.cwd(self.upload_directory)
                    with open(filename, 'rb') as f:
                        totalSize = os.path.getsize(filename)
                        print('Total file size : ' + str(round(totalSize / 1024 / 1024 ,1)) + ' Mb', flush=True)
                        uploadTracker = FtpUploadTracker(int(totalSize))

                        # Get file size if exists
                        files_list = ftp.nlst()
                        print(files_list, flush=True)
                        if os.path.basename(filename) in files_list:
                            print("Resuming", flush=True)
                            ftp.voidcmd('TYPE I')
                            rest_pos = ftp.size(os.path.basename(filename))
                            f.seek(rest_pos, 0)
                            print("seek to " + str(rest_pos))
                            uploadTracker.sizeWritten = rest_pos
                            print(ftp.storbinary('STOR ' + os.path.basename(filename), f, blocksize=1024, callback=uploadTracker.handle, rest=rest_pos), flush=True)
                        else:
                            print(ftp.storbinary('STOR ' + os.path.basename(filename), f, 1024, uploadTracker.handle), flush=True)
                            done = True

            except (BrokenPipeError, ftplib.error_temp, socket.gaierror) as e:
                print(str(type(e)) + ": " + str(e))
                print("connection died, trying again")
                time.sleep(30)


        end_time = time.time()
        print("--- filename = %s ---" % (filename))
        print("--- %s attempts ---" % (tries))
        print("--- %s seconds ---" % (end_time - start_time))
        print("Done")

    def DownloadFiles(self, files_download):
        i = 0
        while i < len(files_download):
            print(files_download[i])
            t1 = threading.Thread(target=self.DownloadFile(files_download[i]))
            t1.start()
            i += 1

    def UploadFiles(self, files_upload):
        i = 0
        while i < len(files_upload):
            print(files_upload[i])
            t1 = threading.Thread(target=self.UploadFile(files_upload[i]))
            t1.start()
            i += 1

if __name__ == "__main__":
    obj = PyFTPclient('speedtest.tele2.net', 21, 'anonymous', 'anonymous@domain.com', '/upload')

    files_download = ['512KB.zip', '5MB.zip', '2MB.zip', '3MB.zip', '20MB.zip', '1MB.zip', '1KB.zip', '10MB.zip', '50MB.zip', '100MB.zip']
    files_upload = ['file1.txt', 'file2.txt', 'file3.txt', 'file4.txt', 'file5.txt', 'file6.txt', 'file7.txt', 'file8.txt', 'file9.txt', 'file10.txt']
    obj.DownloadFiles(files_download)
    obj.UploadFiles(files_upload)
