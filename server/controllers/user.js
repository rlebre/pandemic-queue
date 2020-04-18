const User = require('../models/user');
const { normalizeErrors } = require('../helpers/mongoose');
const jwt = require('jsonwebtoken');
const config = require('../config');

exports.auth = function (req, res) {
    const { username, email, password } = req.body;

    if (!password || (!email && !username)) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide username or email and password." }] });
    }

    User.findOne({
        $or: [
            { email },
            { username }
        ]
    }, function (err, user) {
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
            }, config.SECRET, { expiresIn: '24h' });

            return res.json({ 'token': token });
        } else {
            return res.status(422).send({ errors: [{ title: 'Wrong data!', detail: 'Wrong email or password.' }] });
        }
    });
}

exports.register = function (req, res) {
    const { username, email, password, passwordConfirmation } = req.body;

    if (!password || !email) {
        return res.status(422).send({ errors: [{ title: "Data missing!", detail: "Provide username or email and password." }] });
    }

    if (password !== passwordConfirmation) {
        return res.status(422).send({ errors: [{ title: "Invalid password!", detail: "Passwords don't match." }] });
    }

    User.findOne({
        $or: [
            { email },
            { username }
        ]
    }, function (err, existingUser) {
        if (err) {
            return res.status(422).send({ errors: normalizeErrors(err.errors) });
        }

        if (existingUser) {
            return res.status(422).send({ errors: [{ title: "Invalid username or email!", detail: "Username or email is already in use." }] });
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

function parseToken(token) {
    return jwt.verify(token.split(' ')[1], config.SECRET);
}

function notAuthorized(res) {
    return res.status(401).send({ errors: [{ title: "Not authorized.", detail: "You need to login" }] });
}