const Ticket = require('../models/ticket')
const Store = require('../models/store')

const { normalizeErrors } = require('../helpers/mongoose');

const moment = require('moment');

exports.createTicket = function (req, res) {
    const { store, user } = req.body;
    const enteredQueueTimestamp = moment.now();

    if (!user || !store) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide user and store ." }] });
    }

    //mudar para store.findbyid e obter todos os tickets. verificar se ha tickets com o id do user depois é logica

    Ticket.findOne({ user, store, enteredStoreTimestamp: null })
        .populate('store')
        .exec(function (err, existingTicket) {
            if (err) {
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (existingTicket) {
                return res.status(422).send({ errors: [{ title: "Invalid new ticket!", detail: "Ticket for this user already taken in this store." }] });
            }

            console.log(store.nWaiting, store.capacity);
            if (store.nWaiting >= store.capacity) {
                return res.status(422).send({ errors: [{ title: "Queue too long!", detail: "Queue is too long. The store does not accept more people on queue." }] });
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

                Store.findOneAndUpdate(
                    { _id: store._id },
                    {
                        $inc: { nWaiting: 1 },
                        $set: {
                            lastOnQueue: enteredQueueTimestamp
                        },
                        $push: { waitingTickets: ticket }
                    }, function (err, doc) {
                        if (err) return console.log({ error: err });
                        return console.log('Succesfully saved.');
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