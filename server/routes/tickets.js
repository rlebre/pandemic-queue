const express = require("express");
const router = express.Router();
const Ticket = require("../controllers/ticket");
const UserCtrl = require("../controllers/user");

router.post("/ticket", UserCtrl.authMiddleware, Ticket.createTicket);
router.post("/call-ticket", UserCtrl.authMiddleware, Ticket.callTicket);

module.exports = router;