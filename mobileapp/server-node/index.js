var path = require('path');
var express = require('express');
var bodyParser = require('body-parser');
var ejs = require('ejs');
var session = require('express-session');
var cookieParser = require('cookie-parser');
var consts = require('./utils/constants');

var testRoute = require('./routes/testRoute');
var logRoute = require('./routes/logRoute');

// Create our Express application
var app = express();

// Set view engine to ejs
app.set('view engine', 'ejs');
app.engine('html', require('ejs').renderFile);

// Set the static folder of the web application
app.use(express.static(path.join(__dirname, 'public')));

// Use the body-parser package in our application
app.use(bodyParser.urlencoded({
    extended: true
}));
app.use(bodyParser.json());

// Use express session support since OAuth2orize requires it
app.use(session({
    secret: 'password_0',
    saveUninitialized: true,
    resave: true
}));

// Use cookie parser
app.use(cookieParser());

// Use environment defined port or consts.port
var port = process.env.PORT || consts.port;

/*************************************************************/
/*************************************************************/
/*                   RESTFUL API Routes                      */
/*************************************************************/
/*************************************************************/
// Test API Route
app.use('/api/test', testRoute);
// Log API Route
app.use('/api/log', logRoute);

app.listen(port, function() {
    console.log('Start listening on port ' + port);
});
