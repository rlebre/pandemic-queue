const Store = require('../models/store');
const { normalizeErrors } = require('../helpers/mongoose');

exports.subscribe = function (req, res) {
    const { store } = req.body;
    const user = res.locals.user;

    Store.findById(store._id)
        .populate('usersSubscribed')
        .exec(function (err, foundStore) {
            if (err) {
                console.log(err);
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (!foundStore) {
                return res.status(422).send({ errors: [{ title: "Invalid store!", detail: "Store does not exist." }] });
            }

            if (foundStore.usersSubscribed.find(subscribed => subscribed._id.equals(user._id)) &&
                user.storeSubscriptions.find(subscription => subscription._id.equals(foundStore._id))) {
                return res.status(422).send({ errors: [{ title: "Already subscribed!", detail: "User already subscribed this store." }] });
            }

            foundStore.usersSubscribed.push(user);
            foundStore.save();
            user.storeSubscriptions.push(foundStore);
            user.save();

            return res.json({ 'status': 'subscribed' });
        });
}

exports.unsubscribe = function (req, res) {
    const { store } = req.body;
    const user = res.locals.user;

    Store.findById(store._id)
        .populate('usersSubscribed')
        .exec(function (err, foundStore) {
            if (err) {
                console.log(err);
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (!foundStore) {
                return res.status(422).send({ errors: [{ title: "Invalid store!", detail: "Store does not exist." }] });
            }

            if (!foundStore.usersSubscribed.find(subscribed => subscribed._id.equals(user._id)) ||
                !user.storeSubscriptions.find(subscription => subscription._id.equals(foundStore._id))) {
                return res.status(422).send({ errors: [{ title: "No subscription found!", detail: "User has no subscription in this store." }] });
            }

            foundStore.usersSubscribed.pull(user);
            foundStore.save();
            user.storeSubscriptions.pull(foundStore);
            user.save();

            return res.json({ 'status': 'unsubscribed' });
        });
}
