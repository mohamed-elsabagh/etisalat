import pysftp
import time
import traceback
import logging
import threading
from multiprocessing import Pool

cnopts = pysftp.CnOpts()
cnopts.hostkeys = None

def sftpDownload(directory, filename):
    start_time = time.time()
    tried = 0
    done = False
    while tried < 50 and not done:
        try:
            tried += 1
            with pysftp.Connection(host='demo.wftpserver.com', port=2222, username='demo-user', password='demo-user', cnopts=cnopts) as sftp:
                with sftp.cd(directory):             # temporarily chdir to public
                    sftp.get(filename)               # get a remote file
                    done = True
                    end_time = time.time()
                    print("--- filename = %s ---" % (filename))
                    print("--- %s attempts ---" % (tried))
                    print("--- %s seconds ---" % (end_time - start_time))
        except:
            logging.error(traceback.format_exc())
            time.sleep(30)

def sftpUpload(directory, filename):
    start_time = time.time()
    tried = 0
    done = False
    while tried < 50 and not done:
        try:
            tried += 1
            with pysftp.Connection(host='demo.wftpserver.com', port=2222, username='demo-user', password='demo-user', cnopts=cnopts) as sftp:
                with sftp.cd(directory):             # temporarily chdir to public
                    sftp.put(filename)               # upload file to public/ on remote
                    done = True
                    end_time = time.time()
                    print("--- filename = %s ---" % (filename))
                    print("--- %s attempts ---" % (tried))
                    print("--- %s seconds ---" % (end_time - start_time))
        except:
            logging.error(traceback.format_exc())
            time.sleep(30)

if __name__ == "__main__":
    files_download = ['wftpserver-mac-i386.tar.gz', 'wftpserver-linux-64bit.tar.gz', 'wftpserver-solaris-x86.tar.gz', 'manual_en.pdf', 'manual_en.pdf']
    files_upload = ['file1.txt', 'file2.txt', 'file3.txt', 'file4.txt', 'file5.txt']

    # Download threads
    t1 = threading.Thread(target=sftpDownload('/download', files_download[0]))
    t1.setDaemon(True)
    t1.start()
    t2 = threading.Thread(target=sftpDownload('/download', files_download[1]))
    t2.setDaemon(True)
    t2.start()
    t3 = threading.Thread(target=sftpDownload('/download', files_download[2]))
    t3.setDaemon(True)
    t3.start()
    t4 = threading.Thread(target=sftpDownload('/download', files_download[3]))
    t4.setDaemon(True)
    t4.start()
    t5 = threading.Thread(target=sftpDownload('/download', files_download[4]))
    t5.setDaemon(True)
    t5.start()

    # Upload threads
    t6 = threading.Thread(target=sftpUpload('/upload', files_upload[0]))
    t6.setDaemon(True)
    t6.start()
    t7 = threading.Thread(target=sftpUpload('/upload', files_upload[1]))
    t7.setDaemon(True)
    t7.start()
    t8 = threading.Thread(target=sftpUpload('/upload', files_upload[2]))
    t8.setDaemon(True)
    t8.start()
    t9 = threading.Thread(target=sftpUpload('/upload', files_upload[3]))
    t9.setDaemon(True)
    t9.start()
    t10 = threading.Thread(target=sftpUpload('/upload', files_upload[4]))
    t10.setDaemon(True)
    t10.start()
