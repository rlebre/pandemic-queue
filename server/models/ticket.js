const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const ticketSchema = new Schema({
    enteredQueueTimestamp: {
        type: Date,
        required: ["Starting date is required."],
    },
    enteredStoreTimestamp: {
        type: Date,
    },
    user: { type: Schema.Types.ObjectId, ref: 'User' },
    store: { type: Schema.Types.ObjectId, ref: 'Store' }
});

module.exports = mongoose.model("Ticket", ticketSchema);