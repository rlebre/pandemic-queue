const express = require("express");
const router = express.Router();
const Ticket = require("../controllers/ticket");
const UserCtrl = require("../controllers/user");

router.post("/create", UserCtrl.authMiddleware, Ticket.createTicket);
router.post("/call", UserCtrl.authMiddleware, Ticket.callTicket);
router.get("/exist", UserCtrl.authMiddleware, Ticket.existTicket);
router.delete("/cancel", UserCtrl.authMiddleware, Ticket.cancelTicket);

module.exports = router;