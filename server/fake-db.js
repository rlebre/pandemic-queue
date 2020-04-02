const Store = require("./models/store");
const User = require("./models/user");
const Ticket = require("./models/ticket");

const fakeDbData = require('./data.json');

class FakeDb {
    constructor() {
        this.stores = fakeDbData.stores;
        this.users = fakeDbData.users;
        this.tickets = fakeDbData.tickets
    }

    pushUsersToDb() {
        this.users.forEach(user => {
            const newUser = new User(user);
            newUser.save();
        });
    }

    pushStoresToDb() {
        this.stores.forEach(store => {
            const newStore = new Store(store);
            newStore.save();
        });
    }

    async seedDb() {
        await this.cleanDb();
        this.pushUsersToDb();
        this.pushStoresToDb();
    }

    async cleanDb() {
        await User.remove({});
        await Store.remove({});
        await Ticket.remove({});
    }
}

module.exports = FakeDb;