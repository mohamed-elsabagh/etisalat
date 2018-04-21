// Load required packages
var mongoose = require('mongoose');
var db = require('./database');
var constants = require('../utils/constants.js');

// Define our log schema
var LogSchema = new mongoose.Schema({
    name: {
      type: String,
      required: true
    },
    status: {
      type: Number,
      required: true
    }
}, {
    collection: 'logs'
}); // Force name of collection to be logs

// Export the Mongoose model
module.exports = db.localDb.model('Logs', LogSchema);
