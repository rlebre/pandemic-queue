const Ticket = require('../models/ticket')
const Store = require('../models/store')

const { normalizeErrors } = require('../helpers/mongoose');

const moment = require('moment');

exports.createTicket = function (req, res) {
    var { store } = req.body
    const user = res.locals.user;
    const enteredQueueTimestamp = moment.now();

    if (!store) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide store ." }] });
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

            if (existingStore.waitingTickets.find(ticket => ticket.user._id.equals(user._id) && ticket.enteredStoreTimestamp === null)) {
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
    const { store } = req.body;
    const user = res.locals.user;
    const enteredStoreTimestamp = moment.now();

    if (!store) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide store ." }] });
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

            const ticket = existingStore.waitingTickets.find(ticket => ticket.user._id.equals(user._id) && ticket.enteredStoreTimestamp == null);

            if (!ticket) {
                return res.status(422).send({ errors: [{ title: "Invalid ticket!", detail: "Ticket for this user does not exist in this store." }] });
            }

            ticket.enteredStoreTimestamp = enteredStoreTimestamp;

            ticket.save(function (err) {
                if (err) {
                    return res.status(422).send({ errors: normalizeErrors(err.errors) });
                }

                existingStore.lastEnteredStore = enteredStoreTimestamp;
                existingStore.waitingTickets.pull(ticket)
                existingStore.nWaiting = existingStore.waitingTickets.length;
                existingStore.save(function (err, doc) {
                    if (err) {
                        return res.status(422).send({ errors: normalizeErrors(err.errors) });
                    }
                });

                res.json({ 'status': true, 'ticket': 'removed' });
            });
        });
}

exports.checkTicket = function (req, res) {
    const { store } = req.body;
    const user = res.locals.user;

    if (!store) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide store ." }] });
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

            const ticketExists = existingStore.waitingTickets.find(ticket => ticket.user._id.equals(user._id) && ticket.enteredStoreTimestamp == null) ? true : false;

            res.json({ 'status': true, 'ticketExists': ticketExists });
        });
}