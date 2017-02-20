INSERT INTO fulfillment.order_file_templates (id, fileprefix, headerinfile) VALUES ('457ed5b0-80d7-4cb6-af54-e3f6138c8128', 'O', true);
INSERT INTO fulfillment.order_file_columns (id, columnlabel, datafieldlabel, format, include, keypath, nested, openlmisfield, "position", related, relatedkeypath, orderfiletemplateid) VALUES ('33b2d2e9-3167-46b0-95d4-1295be9afc21', 'Order number', 'fulfillment.header.order.number', NULL, true, 'orderCode', 'order', true, 1, NULL, NULL, '457ed5b0-80d7-4cb6-af54-e3f6138c8128');
INSERT INTO fulfillment.order_file_columns (id, columnlabel, datafieldlabel, format, include, keypath, nested, openlmisfield, "position", related, relatedkeypath, orderfiletemplateid) VALUES ('6b8d331b-a0dd-4a1f-aafb-40e6a72ab9f6', 'Facility code', 'fulfillment.header.facility.code', NULL, true, 'facilityId', 'order', true, 2, 'Facility', 'code', '457ed5b0-80d7-4cb6-af54-e3f6138c8128');
INSERT INTO fulfillment.order_file_columns (id, columnlabel, datafieldlabel, format, include, keypath, nested, openlmisfield, "position", related, relatedkeypath, orderfiletemplateid) VALUES ('752cda76-0db5-4b6e-bb79-0f531ab78e2e', 'Product code', 'fulfillment.header.product.code', NULL, true, 'orderableId', 'lineItem', true, 3, 'Orderable', 'productCode', '457ed5b0-80d7-4cb6-af54-e3f6138c8128');
INSERT INTO fulfillment.order_file_columns (id, columnlabel, datafieldlabel, format, include, keypath, nested, openlmisfield, "position", related, relatedkeypath, orderfiletemplateid) VALUES ('9e825396-269d-4873-baa4-89054e2722f5', 'Product name', 'fulfillment.header.product.name', NULL, true, 'orderableId', 'lineItem', true, 4, 'Orderable', 'name', '457ed5b0-80d7-4cb6-af54-e3f6138c8128');
INSERT INTO fulfillment.order_file_columns (id, columnlabel, datafieldlabel, format, include, keypath, nested, openlmisfield, "position", related, relatedkeypath, orderfiletemplateid) VALUES ('cd57f329-f549-4717-882e-ecbf98122c39', 'Approved quantity', 'fulfillment.header.approved.quantity', NULL, true, 'approvedQuantity', 'lineItem', true, 5, NULL, NULL, '457ed5b0-80d7-4cb6-af54-e3f6138c8128');
INSERT INTO fulfillment.order_file_columns (id, columnlabel, datafieldlabel, format, include, keypath, nested, openlmisfield, "position", related, relatedkeypath, orderfiletemplateid) VALUES ('d0e1aec7-1556-4dc1-8e21-d80a2d76b678', 'Period', 'fulfillment.header.period', 'MM/yy', true, 'processingPeriodId', 'order', true, 6, 'ProcessingPeriod', 'startDate', '457ed5b0-80d7-4cb6-af54-e3f6138c8128');
INSERT INTO fulfillment.order_file_columns (id, columnlabel, datafieldlabel, format, include, keypath, nested, openlmisfield, "position", related, relatedkeypath, orderfiletemplateid) VALUES ('dab6eec0-4cb4-4d4c-94b7-820308da73ff', 'Order date', 'fulfillment.header.order.date', 'dd/MM/yy', true, 'createdDate', 'order', true, 7, NULL, NULL, '457ed5b0-80d7-4cb6-af54-e3f6138c8128');
INSERT INTO fulfillment.order_number_configurations (id, includeordernumberprefix, includeprogramcode, includetypesuffix, ordernumberprefix) VALUES ('70543032-b131-4219-b44d-7781d29db330', true, false, true, 'ORDER-');
INSERT INTO fulfillment.orders (SELECT * FROM json_populate_recordset(NULL::fulfillment.orders, '[{"id":"35e5059a-fd92-4078-a448-b00402b3fb5b","emergency":"false","status":"RECEIVED","externalid":"f152b9bb-c1e2-40b6-b381-28d739208662","createddate":"2016-04-30T16:45:22","createdbyid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","facilityid":"7938919f-6f61-4d1a-a4dc-923c31e9cd45","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","processingperiodid":"516ac930-0d28-49f5-a178-64764e22b236","requestingfacilityid":"7938919f-6f61-4d1a-a4dc-923c31e9cd45","receivingfacilityid":"7938919f-6f61-4d1a-a4dc-923c31e9cd45","supplyingfacilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","ordercode":"ORDER-00000000-0000-0000-0000-000000000006R","quotedcost":1000000},{"id":"5edbb162-721d-4abb-adbf-09ab6130d8e6","emergency":"false","status":"SHIPPED","externalid":"89bd3422-9ada-4369-8ee3-5747fa79c92d","createddate":"2016-06-13T12:22:20","createdbyid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","facilityid":"13037147-1769-4735-90a7-b9b310d128b8","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","processingperiodid":"f419bcd8-ac7a-4b75-8c84-c065ef437938","requestingfacilityid":"13037147-1769-4735-90a7-b9b310d128b8","receivingfacilityid":"13037147-1769-4735-90a7-b9b310d128b8","supplyingfacilityid":"9318b2ea-9ae0-42b2-a7e1-353683f54000","ordercode":"ORDER-00000000-0000-0000-0000-000000000007R","quotedcost":30000},{"id":"90ddacf9-2280-4e8c-80f7-4e2e0251f8f3","emergency":"false","status":"PICKING","externalid":"276353fb-970f-4638-8ead-3e97c9450aa3","createddate":"2016-06-15T12:00:00","createdbyid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","facilityid":"7938919f-6f61-4d1a-a4dc-923c31e9cd45","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","processingperiodid":"04ec3c83-a086-4792-b7a3-c46559b7f6cd","requestingfacilityid":"7938919f-6f61-4d1a-a4dc-923c31e9cd45","receivingfacilityid":"7938919f-6f61-4d1a-a4dc-923c31e9cd45","supplyingfacilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","ordercode":"ORDER-00000000-0000-0000-0000-000000000008R","quotedcost":5000},{"id":"ec49baf1-fb6c-4bbc-ad5e-54fff70115a2","emergency":"false","status":"ORDERED","externalid":"fbb8d966-e3d1-42e5-825f-f75dd63b9a0a","createddate":"2016-06-21T17:17:17","createdbyid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","facilityid":"13037147-1769-4735-90a7-b9b310d128b8","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","processingperiodid":"c5012ddf-b145-4eee-9640-b2cbf4f2da85","requestingfacilityid":"13037147-1769-4735-90a7-b9b310d128b8","receivingfacilityid":"13037147-1769-4735-90a7-b9b310d128b8","supplyingfacilityid":"9318b2ea-9ae0-42b2-a7e1-353683f54000","ordercode":"ORDER-00000000-0000-0000-0000-000000000009R","quotedcost":100000}]'));
INSERT INTO fulfillment.order_line_items (SELECT * FROM json_populate_recordset(NULL::fulfillment.order_line_items, '[{"id":"9a4c65f9-f834-44bb-9e75-2c18c9a2a9cf","orderid":"35e5059a-fd92-4078-a448-b00402b3fb5b","orderableid":"d602d0c6-4052-456c-8ccd-61b4ad77bece","orderedquantity":50,"filledquantity":100,"approvedquantity":0,"packstoship":0},{"id":"7b1b735d-c29a-4486-8352-04d3034e0d77","orderid":"35e5059a-fd92-4078-a448-b00402b3fb5b","orderableid":"23819693-0670-4c4b-b400-28e009b86b51","orderedquantity":0,"filledquantity":50,"approvedquantity":0,"packstoship":0},{"id":"5a38bb94-b206-4575-9555-883d4eecc5cc","orderid":"5edbb162-721d-4abb-adbf-09ab6130d8e6","orderableid":"2400e410-b8dd-4954-b1c0-80d8a8e785fc","orderedquantity":0,"filledquantity":800,"approvedquantity":0,"packstoship":0},{"id":"657686c4-dbf7-4c1d-b491-1f650002fa39","orderid":"5edbb162-721d-4abb-adbf-09ab6130d8e6","orderableid":"103396e1-f5e4-4754-bb7b-1c799097dfea","orderedquantity":0,"filledquantity":250,"approvedquantity":0,"packstoship":0},{"id":"c57e407a-7bad-4e52-aee4-6f1742468d61","orderid":"90ddacf9-2280-4e8c-80f7-4e2e0251f8f3","orderableid":"d602d0c6-4052-456c-8ccd-61b4ad77bece","orderedquantity":50,"filledquantity":50,"approvedquantity":0,"packstoship":0},{"id":"0e30fa6e-dca8-4b43-aa78-bf5fc3a09ef3","orderid":"90ddacf9-2280-4e8c-80f7-4e2e0251f8f3","orderableid":"23819693-0670-4c4b-b400-28e009b86b51","orderedquantity":0,"filledquantity":70,"approvedquantity":0,"packstoship":0},{"id":"241e50a9-14c3-4b04-b161-915aa20d49da","orderid":"ec49baf1-fb6c-4bbc-ad5e-54fff70115a2","orderableid":"2400e410-b8dd-4954-b1c0-80d8a8e785fc","orderedquantity":0,"filledquantity":30,"approvedquantity":0,"packstoship":0},{"id":"739a2eb5-705a-4d0a-a7e9-a01146e1441b","orderid":"ec49baf1-fb6c-4bbc-ad5e-54fff70115a2","orderableid":"103396e1-f5e4-4754-bb7b-1c799097dfea","orderedquantity":0,"filledquantity":1200,"approvedquantity":0,"packstoship":0}]'));
INSERT INTO fulfillment.proof_of_deliveries (SELECT * FROM json_populate_recordset(NULL::fulfillment.proof_of_deliveries, '[{"id":"b204aa9e-5c95-4962-b418-571dbdf7796e","orderid":"35e5059a-fd92-4078-a448-b00402b3fb5b","deliveredby":"Delivery Guy","receivedby":"Receiver","receiveddate":"2016-05-13"}]'));
INSERT INTO fulfillment.transfer_properties (SELECT * FROM json_populate_recordset(NULL::fulfillment.transfer_properties, '[{"id":"5f028911-2683-4343-83e7-48434cd6b7a2","type":"ftp","protocol":"FTP","username":"admin","password":"p@ssw0rd","facilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","serverhost":"ftp","serverport":21,"remotedirectory":"/orders/files/csv/HC01","localdirectory":"/var/lib/openlmis/fulfillment/orders/HC01","passivemode":true},{"id":"b8f940de-3f01-4e56-b164-8b9651ecd38e","type":"ftp","protocol":"FTP","username":"admin","password":"p@ssw0rd","facilityid":"13037147-1769-4735-90a7-b9b310d128b8","serverhost":"ftp","serverport":21,"remotedirectory":"/orders/files/csv/DH01","localdirectory":"/var/lib/openlmis/fulfillment/orders/DH01","passivemode":true},{"id":"ce88d3b1-0518-42fb-8fc1-e47ad80c7134","type":"ftp","protocol":"FTP","username":"admin","password":"p@ssw0rd","facilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","serverhost":"ftp","serverport":21,"remotedirectory":"/orders/files/csv/W01","localdirectory":"/var/lib/openlmis/fulfillment/orders/W01","passivemode":true},{"id":"643c433a-91fe-4373-85f1-2db751d5be06","type":"ftp","protocol":"FTP","username":"admin","password":"p@ssw0rd","facilityid":"44a29ccb-ed55-4c26-9eca-7e5759269b25","serverhost":"ftp","serverport":21,"remotedirectory":"/orders/files/csv/FAC003","localdirectory":"/var/lib/openlmis/fulfillment/orders/FAC003","passivemode":true},{"id":"312247e0-7c31-4b6c-b35c-1868e5a19742","type":"ftp","protocol":"FTP","username":"admin","password":"p@ssw0rd","facilityid":"ab95f1cf-ae8d-4888-ba9d-05141d3f50de","serverhost":"ftp","serverport":21,"remotedirectory":"/orders/files/csv/FAC004","localdirectory":"/var/lib/openlmis/fulfillment/orders/FAC004","passivemode":true}]'));
INSERT INTO fulfillment.proof_of_delivery_line_items (SELECT * FROM json_populate_recordset(NULL::fulfillment.proof_of_delivery_line_items, '[{"id":"bcc0fe6d-7063-4567-a461-32c9c2e0d744","orderlineitemid":"9a4c65f9-f834-44bb-9e75-2c18c9a2a9cf","proofofdeliveryid":"b204aa9e-5c95-4962-b418-571dbdf7796e","quantityshipped":100,"quantityreceived":100,"quantityreturned":0,"replacedproductcode":"P1"},{"id":"9021d448-f4e2-4aca-bb0d-9894f7780199","orderlineitemid":"7b1b735d-c29a-4486-8352-04d3034e0d77","proofofdeliveryid":"b204aa9e-5c95-4962-b418-571dbdf7796e","quantityshipped":50,"quantityreceived":50,"quantityreturned":0,"replacedproductcode":"P2"}]'));
INSERT INTO fulfillment.configuration_settings (SELECT * FROM json_populate_recordset(NULL::fulfillment.configuration_settings, '[{"key":"fulfillment.email.noreply","value":"noreply@openlmis.org"},{"key":"fulfillment.email.order-creation.subject","value":"New order"},{"key":"fulfillment.email.order-creation.body","value":"Create an order: {id} with status: {status}"}]'));
