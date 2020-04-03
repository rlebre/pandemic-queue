const express = require("express");
const router = express.Router();
const Store = require("../controllers/store");

router.post("/store", Store.createStore);
router.get("/number-waiting-tickets", Store.getNumberStoreWaitingTickets);
router.get("/waiting-tickets", Store.getStoreWaitingTickets);
router.get("/get-store-details", Store.getStoreDetails);

module.exports = router;