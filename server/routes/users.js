const express = require("express");
const router = express.Router();
const User = require("../controllers/user");

router.post("/auth", User.auth);
router.post("/register", User.register);
router.get("/get-user-details", User.authMiddleware, User.getUserDetails);

module.exports = router;