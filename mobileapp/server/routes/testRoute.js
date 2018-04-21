var express = require('express');

// Create our Express router
var router = express.Router();

/********************************/
/*       Test Routes            */
/********************************/
// Create endpoint handlers for /api/test
router.route('/')
    .get(function(req, res) {
        res.json({
            message: 'Testing route is working'
        });
    });

module.exports = router;
