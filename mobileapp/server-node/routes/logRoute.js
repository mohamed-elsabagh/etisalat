var express = require('express');
var logController = require('../controllers/logController');

// Create our Express router
var router = express.Router();

/********************************/
/*        Log Routes           */
/********************************/
// Create endpoint handlers for /api/log
router.route('/')
    .get(logController.getLogs)
    .post(logController.postLog);

module.exports = router;
