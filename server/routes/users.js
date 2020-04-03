const express = require("express");
const router = express.Router();
const User = require("../controllers/user");

router.post("/auth", User.auth);
router.post("/register", User.register);

router.get("/get-user-details", User.authMiddleware, User.getUserDetails);
router.post("/subscribe", User.authMiddleware, User.subscribe);
router.post("/unsubscribe", User.authMiddleware, User.unsubscribe);

module.exports = router;