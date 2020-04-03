const Ticket = require('../models/ticket')
const Store = require('../models/store')

const { normalizeErrors } = require('../helpers/mongoose');

const moment = require('moment');

exports.createTicket = function (req, res) {
    var { store, user } = req.body;
    const enteredQueueTimestamp = moment.now();

    if (!user || !store) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide user and store ." }] });
    }

    Store.findById(store._id)
        .populate('waitingTickets')
        .exec((err, existingStore) => {
            if (err) {
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (!existingStore) {
                return res.status(422).send({ errors: [{ title: "Invalid store!", detail: "Store does not exist!" }] });
            }

            if (existingStore.waitingTickets.find(ticket => ticket.user._id == user._id && ticket.enteredStoreTimestamp == null)) {
                return res.status(422).send({ errors: [{ title: "Invalid new ticket!", detail: "Ticket for this user already taken in this store." }] });
            }

            const ticket = new Ticket(
                {
                    enteredQueueTimestamp,
                    enteredStoreTimestamp: null,
                    user,
                    store
                }
            )

            ticket.save(function (err) {
                if (err) {
                    return res.status(422).send({ errors: normalizeErrors(err.errors) });
                }

                existingStore.lastOnQueue = enteredQueueTimestamp;
                existingStore.waitingTickets.push(ticket)
                existingStore.nWaiting = existingStore.waitingTickets.length;
                existingStore.save(function (err, doc) {
                    if (err) {
                        return res.status(422).send({ errors: normalizeErrors(err.errors) });
                    }
                });

                res.json({ 'status': true, 'ticket': 'added' });
            });
        });
}

exports.callTicket = function (req, res) {
    const { store, user } = req.body;
    const enteredStoreTimestamp = moment.now();

    if (!user || !store) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide user and store ." }] });
    }

    Ticket.findOne({ user, store, enteredStoreTimestamp: null })
        .populate('store')
        .exec(function (err, existingTicket) {
            if (err) {
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (!existingTicket) {
                return res.status(422).send({ errors: [{ title: "Invalid ticket!", detail: "Ticket for this user does not exist in this store." }] });
            }

            existingTicket.enteredStoreTimestamp = enteredStoreTimestamp;

            existingTicket.save(function (err) {
                if (err) {
                    return res.status(422).send({ errors: normalizeErrors(err.errors) });
                }

                Store.findOneAndUpdate(
                    { _id: store._id },
                    {
                        $inc: { nWaiting: -1 },
                        $set: {
                            lastEnteredStore: enteredStoreTimestamp,
                        },
                        $pull: {
                            waitingTickets: existingTicket._id
                        }
                    }, function (err, doc) {
                        if (err) return console.log({ error: err });
                        return console.log('Succesfully saved.');
                    });

                res.json({ 'status': true, 'ticket': 'removed' });
            });
        });
}

function ticketExists(userId, ticket) {
    for (var i = 0; i < myArray.length; i++) {
        if (myArray[i].name === nameKey) {
            return myArray[i];
        }
    }
    return ticket.user._id === userId && ticket.enteredStoreTimestamp === null;
}