create table order_product(
                              order_product_id bigint primary key,
                              order_id bigint,
                              order_product_qty int,
                              order_product_name varchar(255),
                              product_id bigint,
                              order_product_price int,
                              order_product_main_img varchar(255)
);

create table order_list(
                           order_id bigint primary key,
                           user_id bigint,
                           order_date varchar(255),
                           order_status varchar(255),
                           order_total_price int,
                           order_recipient_name varchar(255),
                           order_address varchar(255),
                           order_phone varchar(255),
                           order_request varchar(255),
                           order_seller_id bigint,
                           order_delivery_status bigint,
                           order_delivery_waybill_number varchar(255),
                           order_delivery_company varchar(255),
                           order_delivery_date varchar(255)
);