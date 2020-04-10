const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const storeSchema = new Schema({
    name: {
        type: String,
        max: [32, "Name too long. Max: 32 characters."],
        min: [4, "Name too long. Max: 4 characters."],
        required: true,
        lowercase: true
    },
    city: {
        type: String,
        required: true,
        lowercase: true
    },
    address: {
        type: String,
        required: true,
        lowercase: true
    },
    nWaiting: {
        type: Number,
        max: [50, "Number of people waiting is too long."],
        min: [0, "Impossible to have negative number of people."],
        required: true
    },
    lastOnQueue: { type: Date },
    lastEnteredStore: { type: Date },
    capacity: { type: Number },
    waitingTickets: [{ type: Schema.Types.ObjectId, ref: 'Ticket' }],
    usersSubscribed: [{ type: Schema.Types.ObjectId, ref: 'User' }],
    parentStore: {
        type: String,
        required: true,
        lowercase: true
    },
});


module.exports = mongoose.model("Store", storeSchema);