const express = require("express");
const router = express.Router();
const Ticket = require("../controllers/ticket");

router.post("/ticket", Ticket.createTicket);
router.post("/call-ticket", Ticket.callTicket);

module.exports = router;