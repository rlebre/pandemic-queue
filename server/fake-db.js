const Store = require("./models/store");
const User = require("./models/user");

const storesContinente = require('./data/stores/continente.json');
const storesAuchan = require('./data/stores/auchan.json');
const storesLidl = require('./data/stores/lidl.json');
const storesMeusuper = require('./data/stores/meusuper.json');
const storesPingodoce = require('./data/stores/pingodoce.json');

const users = require('./data/users/users.json');


class FakeDb {
    constructor() {
    }

    pushUsersToDb() {
        this.users.forEach(user => {
            const newUser = new User(user);
            newUser.save();
        });
    }

    saveStore(name, city, address, capacity, parentStore) {
        const newStore = new Store(
            {
                name,
                city,
                address,
                nWaiting: 0,
                lastOnQueue: null,
                lastEnteredStore: null,
                capacity,
                nWaitingTickets: [],
                parentStore
            });

        newStore.save();
    }

    pushPingoDoceStores() {
        var count = 0;
        storesPingodoce.forEach(store => {
            var name = "Pingo Doce " + store.name;
            var city = store.county;
            var address = store.address + " " + store.postal_code;
            var capacity = parseInt(store.id);
            var parentStore = "pingodoce";

            this.saveStore(name, city, address, capacity, parentStore);
            count++;
        });
        console.log("Pingo Doce stores pushed: ", count);
    }

    pushMeuSuperStores() {
        var count = 0;
        storesMeusuper.forEach(store => {
            var name = "Meu Super " + store.name;
            var city = store.name;
            var address = store.morada;
            var capacity = Math.floor(parseInt(store.id) / 10);
            var parentStore = "meusuper";

            this.saveStore(name, city, address, capacity, parentStore);
            count++;
        });
        console.log("Meu Super stores pushed: ", count);
    }

    pushLidlStores() {
        var count = 0;
        storesLidl.forEach(store => {
            var name = "Lidl" + store.Locality;
            var city = store.Locality;
            var address = store.AddressLine + ", " + store.PostalCode;
            var capacity = parseInt(store.EntityID);
            var parentStore = "lidl";

            this.saveStore(name, city, address, capacity, parentStore);
            count++;
        });
        console.log("Lidl stores pushed: ", count);
    }

    pushAuchanStores() {
        var count = 0;
        storesAuchan.forEach(store => {
            var name = store.DisplayName;
            var city = store.Address.City;
            var address = store.Address.FullAddress;
            var capacity = 500;
            var parentStore = "auchan";

            this.saveStore(name, city, address, capacity, parentStore);
            count++;
        });
        console.log("Auchan stores pushed: ", count);
    }

    pushContinenteStores() {
        var count = 0;
        storesContinente.forEach(store => {
            var name = store.name;
            var city = store.city;
            var address = store.streetAndNumber + " " + store.zip;
            var capacity = parseInt(store.identifier);
            var parentStore = "continente";

            this.saveStore(name, city, address, capacity, parentStore);
            count++;
        });
        console.log("Continente stores pushed: ", count);
    }

    pushUsersToDb() {
        var count = 0;
        users.forEach(user => {
            const newUser = new User(user);
            newUser.save();
            count++;
        });
        console.log("Users pushed: ", count);
    }

    pushStoresToDb() {
        this.pushAuchanStores();
        this.pushContinenteStores();
        this.pushLidlStores();
        this.pushMeuSuperStores();
        this.pushPingoDoceStores();
    }

    async seedDb() {
        console.log('\nCleaning database...');
        await this.cleanDb();


        console.log('\nPushing stores to database...');
        this.pushStoresToDb();

        console.log('\nPushing users to database...');
        this.pushUsersToDb();


        console.log('\nDatabase initialized successffuly.');
    }

    async cleanDb() {
        await Store.deleteMany({});
        await User.deleteMany({});
    }
}

module.exports = FakeDb;