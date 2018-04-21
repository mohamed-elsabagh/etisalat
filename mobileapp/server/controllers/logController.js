// Load required packages
var Log = require('../models/logModel');
var mongoose = require('mongoose');

exports.getLogs = function(req, res) {
  try {
    // Use the Log model to find all log
    Log.find({}, function(err, logs) {
      if (err) {
        res.status(400).json({
          error: err
        });
        return;
      }

      res.json(logs);
    });
  } catch (e) {
    res.status(400).json({
      error: e
    });
  }
};

exports.postLog = function(req, res) {
  try {

    // Use the Log model to find all log
    Log.findOne({ 'name': req.body.name }, function(err, log) {
      if (err) {
        res.status(400).json({
          error: err
        });
        return;
      }

      if (log) {
        // the process exist before, just update
        log.status = req.body.status;
        log.save(function(err, new_log) {
          if (err) {
            res.status(400).json({
              error: err
            });
            return;
          }

          res.json(new_log);
        });
        return;
      } else {
        // create new log
        let new_log = new Log();
        new_log.name = req.body.name;
        new_log.status = req.body.status;
        new_log.save(function(err, new_log) {
          if (err) {
            res.status(400).json({
              error: err
            });
            return;
          }

          res.json(new_log);
        });
      }
    });
  } catch (e) {
    res.status(400).json({
      error: e
    });
  }
};
