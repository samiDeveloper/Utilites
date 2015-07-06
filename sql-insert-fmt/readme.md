This project exists because I don't like counting comma separated SQL stuff.

Today many systems still use a SQL database. Very often existing software connecting to the database comes with SQL insert statements to setup test data. This project formats the SQL to a less unreadable form. It aligns the column names and the values. This allows one to match them more easily.

It is under construction, working on the UI. The SQL parser and formatter both work. Check out the unit tests.

**Example**

Source:

    INSERT INTO Customer (Id, CustomerName, BithDate, ContactName, Address, City, PostalCode, Country, Phone) values (87, 'Wartian Herkku', to_date('1972-04-01', 'yyyy-mm-dd'), 'Pirkko Koskitalo', 'Torikatu 38', 'Ouluoulu', '90110', 'Finland', '+12312249090');

Result:

    INSERT INTO Customer (
        Id, CustomerName,     BithDate,                            ContactName,
        Address,       City,       PostalCode, Country,   Phone
    ) values (
        87, 'Wartian Herkku', to_date('1972-04-01', 'yyyy-mm-dd'), 'Pirkko Koskitalo',
        'Torikatu 38', 'Ouluoulu', '90110',    'Finland', '+12312249090'
    );

**Resources**

 * <http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt>
 * <http://www.antlr.org/>
 * <https://github.com/antlr/grammars-v4/tree/master/sqlite>
