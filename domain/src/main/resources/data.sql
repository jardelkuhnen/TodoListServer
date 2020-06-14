
-- GENERATE BACKUP
-- select 'insert into orders (description, register_date) values (''', description, '''', ',''', register_date, ''')' from orders
-- select 'insert into order_item(description, is_finished, register_date) values (''', description, '''', ',''', is_finished, '''', ',''', register_date, ''')', order_id from order_item


select * from orders;