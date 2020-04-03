const User = require('../models/user');
const Store = require('../models/store');
const { normalizeErrors } = require('../helpers/mongoose');
const jwt = require('jsonwebtoken');
const config = require('../config');

exports.auth = function (req, res) {
    const { email, password } = req.body;

    if (!password || !email) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide email and password." }] });
    }

    User.findOne({ email }, function (err, user) {
        if (err) {
            return res.status(422).send({ errors: normalizeErrors(err.errors) });
        }

        if (!user) {
            return res.status(422).send({ errors: [{ title: 'Invalid User!', detail: 'User does not exist.' }] });
        }

        if (user.hasSamePassword(password)) {
            const token = jwt.sign({
                userId: user.id,
                username: user.username
            }, config.SECRET, { expiresIn: '1h' });

            return res.json(token);
        } else {
            return res.status(422).send({ errors: [{ title: 'Wrong data!', detail: 'Wrong email or password.' }] });
        }
    });
}

exports.register = function (req, res) {
    const { username, email, password, passwordConfirmation } = req.body;

    if (!password || !email) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide email and password." }] });
    }

    if (password !== passwordConfirmation) {
        return res.status(422).send({ errors: [{ title: "Invalid password!", detail: "Passwords don't match." }] });
    }

    User.findOne({ email }, function (err, existingUser) {
        if (err) {
            return res.status(422).send({ errors: normalizeErrors(err.errors) });
        }

        if (existingUser) {
            return res.status(422).send({ errors: [{ title: "Invalid email!", detail: "Email is already in use." }] });
        }

        const user = new User({
            username,
            email,
            password
        });

        user.save(function (err) {
            if (err) {
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            res.json({ 'registered': true });
        });
    });
}

exports.authMiddleware = function (req, res, next) {
    const token = req.headers.authorization;

    if (token) {
        const user = parseToken(token);

        User.findById(user.userId, function (err, user) {
            if (err) {
                return res.status(422).send({ errors: normalizeErrors(err.errors) });
            }

            if (user) {
                res.locals.user = user;
                next();
            } else {
                return notAuthorized(res);
            }
        });
    } else {
        return notAuthorized(res);
    }
}

exports.getUserDetails = function (req, res) {
    return res.json({ 'user': res.locals.user });
}

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

function parseToken(token) {
    return jwt.verify(token.split(' ')[1], config.SECRET);
}

function notAuthorized(res) {
    return res.status(401).send({ errors: [{ title: "Not authorized.", detail: "You need to login" }] });
}