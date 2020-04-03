const express = require("express");
const router = express.Router();
const User = require("../controllers/user");
const Subscription = require("../controllers/subscription");

router.post("/subscribe", User.authMiddleware, Subscription.subscribe);
router.post("/unsubscribe", User.authMiddleware, Subscription.unsubscribe);

module.exports = router;