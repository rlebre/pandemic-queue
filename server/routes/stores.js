const express = require("express");
const router = express.Router();
const Store = require("../controllers/store");

router.post("/create", Store.createStore);
router.get("/number-waiting-tickets", Store.getNumberStoreWaitingTickets);
router.get("/waiting-tickets", Store.getStoreWaitingTickets);
router.get("/get-store-details", Store.getStoreDetails);
router.get("/get-store-list", Store.getStoreList);

module.exports = router;