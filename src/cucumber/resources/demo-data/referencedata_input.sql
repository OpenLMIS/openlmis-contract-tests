INSERT INTO referencedata.users (id, username, firstName, lastName, email, timezone) VALUES ('35316636-6264-6331-2d34-3933322d3462', 'admin', 'Admin', 'User', 'example@mail.com', 'UTC');
INSERT INTO referencedata.rights (id, name, type) VALUES ('e96017ff-af8c-4313-a070-caa70465c949', 'FACILITIES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('4e731cf7-854f-4af7-9ea4-bd5d8ed7bb22', 'GEOGRAPHIC_ZONE_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('5c4b3b9b-713e-4b9a-8c58-7efcd2954512', 'SUPERVISORY_NODE_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('fb6a0053-6254-4b41-8028-bf91421f90dd', 'PRODUCT_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('8816edba-b8a9-11e6-80f5-76304dec7eb7', 'REQUISITION_TEMPLATES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('4bed4f40-36b5-42a7-94c9-0fd3d4252374', 'STOCK_CARD_TEMPLATES_MANAGE', 'GENERAL_ADMIN');
INSERT INTO referencedata.rights (id, name, type) VALUES ('9ade922b-3523-4582-bef4-a47701f7df14', 'REQUISITION_CREATE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('bffa2de2-dc2a-47dd-b126-6501748ac3fc', 'REQUISITION_APPROVE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('feb4c0b8-f6d2-4289-b29d-811c1d0b2863', 'REQUISITION_AUTHORIZE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('c3eb5df0-c3ac-4e70-a978-02827462f60e', 'REQUISITION_DELETE', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('e101d2b8-6a0f-4af6-a5de-a9576b4ebc50', 'REQUISITION_VIEW', 'SUPERVISION');
INSERT INTO referencedata.rights (id, name, type) VALUES ('7958129d-c4c0-4294-a40c-c2b07cb8e515', 'REQUISITION_CONVERT_TO_ORDER', 'ORDER_FULFILLMENT');
INSERT INTO referencedata.rights (id, name, type) VALUES ('65626c3d-513f-4255-93fd-808709860594', 'FULFILLMENT_TRANSFER_ORDER', 'ORDER_FULFILLMENT');
INSERT INTO referencedata.geographic_levels (SELECT * FROM json_populate_recordset(NULL::referencedata.geographic_levels, '[{"id":"6b78e6c6-292e-4733-bb9c-3d802ad61206","code":"Country","levelnumber":1},{"id":"9b497d87-cdd9-400e-bb04-fae0bf6a9491","code":"Region","levelnumber":2},{"id":"93c05138-4550-4461-9e8a-79d5f050c223","code":"District","levelnumber":3},{"id":"90e35999-a64f-4312-ba8f-bc13a1311c75","code":"City","levelnumber":4}]'));
INSERT INTO referencedata.geographic_zones (SELECT * FROM json_populate_recordset(NULL::referencedata.geographic_zones, '[{"id":"4e471242-da63-436c-8157-ade3e615c848","code":"Mal","name":"Malawi","levelid":"6b78e6c6-292e-4733-bb9c-3d802ad61206"},{"id":"0bbd69c1-e20f-48f5-aae4-26dcd8aa7602","code":"Mal-So","name":"Southern Region","levelid":"9b497d87-cdd9-400e-bb04-fae0bf6a9491"},{"id":"50293982-602a-4046-ac29-d7f83a8a13e9","code":"M-So-Bal","name":"Balaka District","levelid":"93c05138-4550-4461-9e8a-79d5f050c223"},{"id":"bf2b810b-cdbf-48b2-b569-149b3cf42387","code":"M-So-Bal-Bal","name":"Balaka","levelid":"90e35999-a64f-4312-ba8f-bc13a1311c75","latitude":-14.99,"longitude":34.92}]'));
INSERT INTO referencedata.facility_operators (SELECT * FROM json_populate_recordset(NULL::referencedata.facility_operators, '[{"id":"9456c3e9-c4a6-4a28-9e08-47ceb16a4121","code":"moh","name":"Ministry of Health","displayorder":1},{"id":"1074353d-7364-4618-a127-708d7303a231","code":"dwb","name":"Doctors without Borders","displayorder":2}]'));
INSERT INTO referencedata.facility_types (SELECT * FROM json_populate_recordset(NULL::referencedata.facility_types, '[{"id":"ac1d268b-ce10-455f-bf87-9c667da8f060","code":"health_center","name":"Health Center","active":"true","displayorder":1},{"id":"663b1d34-cc17-4d60-9619-e553e45aa441","code":"dist_hosp","name":"District Hospital","active":"true","displayorder":2},{"id":"e2faaa9e-4b2d-4212-bb60-fd62970b2113","code":"warehouse","name":"Warehouse","active":"true","displayorder":3}]'));
INSERT INTO referencedata.facilities (SELECT * FROM json_populate_recordset(NULL::referencedata.facilities, '[{"id":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","code":"HC01","name":"Comfort Health Clinic","typeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","operatedbyid":"9456c3e9-c4a6-4a28-9e08-47ceb16a4121","active":"true","enabled":"true","geographiczoneid":"bf2b810b-cdbf-48b2-b569-149b3cf42387","openlmisaccessible":"true"},{"id":"13037147-1769-4735-90a7-b9b310d128b8","code":"DH01","name":"Balaka District Hospital","geographiczoneid":"50293982-602a-4046-ac29-d7f83a8a13e9","typeid":"663b1d34-cc17-4d60-9619-e553e45aa441","operatedbyid":"9456c3e9-c4a6-4a28-9e08-47ceb16a4121","active":"true","enabled":"true","openlmisaccessible":"true"},{"id":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","code":"W01","name":"CMST Warehouse","geographiczoneid":"0bbd69c1-e20f-48f5-aae4-26dcd8aa7602","typeid":"e2faaa9e-4b2d-4212-bb60-fd62970b2113","operatedbyid":"9456c3e9-c4a6-4a28-9e08-47ceb16a4121","active":"true","enabled":"true","openlmisaccessible":"true"},{"id":"44a29ccb-ed55-4c26-9eca-7e5759269b25","code":"FAC003","name":"Facility Inactive Enabled","geographiczoneid":"0bbd69c1-e20f-48f5-aae4-26dcd8aa7602","typeid":"663b1d34-cc17-4d60-9619-e553e45aa441","operatedbyid":"1074353d-7364-4618-a127-708d7303a231","active":"false","enabled":"true","openlmisaccessible":"false"},{"id":"ab95f1cf-ae8d-4888-ba9d-05141d3f50de","code":"FAC004","name":"Facility Inactive Disabled","geographiczoneid":"0bbd69c1-e20f-48f5-aae4-26dcd8aa7602","typeid":"663b1d34-cc17-4d60-9619-e553e45aa441","operatedbyid":"1074353d-7364-4618-a127-708d7303a231","active":"false","enabled":"false","openlmisaccessible":"true"}]'));
INSERT INTO referencedata.supervisory_nodes (SELECT * FROM json_populate_recordset(NULL::referencedata.supervisory_nodes, '[{"id":"fb38bd1c-beeb-4527-8345-900900329c10","code":"N1","name":"FP Approval point","facilityid":"13037147-1769-4735-90a7-b9b310d128b8"},{"id":"9f470265-0770-4dd1-bd5a-cf8fe3734d79","code":"N1.1","name":"FP Approval sub point","parentid":"fb38bd1c-beeb-4527-8345-900900329c10","facilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce"},{"id":"6d5831e7-c336-4129-bd10-0149910a72aa","code":"N2","name":"EM Approval point","facilityid":"13037147-1769-4735-90a7-b9b310d128b8"},{"id":"0f48d25f-2cc3-4f42-a58b-5c74aab70224","code":"N3","name":"New program approval point","facilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e"}]'));
INSERT INTO referencedata.processing_schedules (SELECT * FROM json_populate_recordset(NULL::referencedata.processing_schedules, '[{"id":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac","code":"SCH001","name":"Monthly","modifieddate":"2015-12-31 23:59:59"},{"id":"057921bd-1841-4748-8523-dbe5ebb58368","code":"SCH002","name":"Quarterly"},{"id":"d036a3bc-865a-402b-972e-96e0695bf8ce","code":"SCH003","name":"Schedule 003","modifieddate":"2016-02-20 13:20:11"},{"id":"9fca3d5d-b2c8-46c8-adb0-96e38d2c1f6f","code":"SCH004","name":"Schedule 004","modifieddate":"2016-04-05 16:57:06"},{"id":"083775ab-b9af-41a3-b845-0fbf4fe1583d","code":"SCH005","name":"Schedule 005","modifieddate":"2016-04-05 16:57:06"}]'));
INSERT INTO referencedata.processing_periods (SELECT * FROM json_populate_recordset(NULL::referencedata.processing_periods, '[{"id":"516ac930-0d28-49f5-a178-64764e22b236","name":"Jan2017","startdate":"2017-01-01","enddate":"2017-01-31","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"04ec3c83-a086-4792-b7a3-c46559b7f6cd","name":"Feb2017","startdate":"2017-02-01","enddate":"2017-02-28","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"61694e82-1be6-40a4-9aaa-bfbb720a0d7d","name":"Mar2017","startdate":"2017-03-01","enddate":"2017-03-31","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"c9287c65-67fa-4958-adb6-52069f2b1379","name":"Apr2017","startdate":"2017-04-01","enddate":"2017-04-30","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"2d490229-02f8-4235-9be4-1443fd8f7b4f","name":"May2017","startdate":"2017-05-01","enddate":"2017-05-31","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"a91dfbdf-0d9b-4cb1-9d46-0a47b17d22d0","name":"Jun2017","startdate":"2017-06-01","enddate":"2017-06-30","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"f55d9dda-d440-40c0-a793-914e0fe1f579","name":"Jul2017","startdate":"2017-07-01","enddate":"2017-07-31","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"ecd6c3c8-f80c-4c4e-af9a-d0529b55424a","name":"Aug2017","startdate":"2017-08-01","enddate":"2017-08-31","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"c08e1295-793f-4550-933b-571e117f15d9","name":"Sep2017","startdate":"2017-09-01","enddate":"2017-09-30","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"2a9fddb6-5dd3-4dde-81f3-c19629257986","name":"Oct2017","startdate":"2017-10-01","enddate":"2017-10-31","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"1f3c89b2-13e4-4a72-9ef2-228302cc5a25","name":"Nov2017","startdate":"2017-11-01","enddate":"2017-11-30","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"96da0fc3-28fd-4a3f-87a8-b5cdb175c45a","name":"Dec2017","startdate":"2017-12-01","enddate":"2017-12-31","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac"},{"id":"f419bcd8-ac7a-4b75-8c84-c065ef437938","name":"Q1","startdate":"2017-01-01","enddate":"2017-03-31","processingscheduleid":"057921bd-1841-4748-8523-dbe5ebb58368"},{"id":"c5012ddf-b145-4eee-9640-b2cbf4f2da85","name":"Q2","startdate":"2017-04-01","enddate":"2017-06-30","processingscheduleid":"057921bd-1841-4748-8523-dbe5ebb58368"},{"id":"65671374-66e2-4a11-a7dc-05b645c6d815","name":"Q3","startdate":"2017-07-01","enddate":"2017-09-30","processingscheduleid":"057921bd-1841-4748-8523-dbe5ebb58368"},{"id":"7880be4f-6582-472a-9ea5-a6baed71e6e5","name":"Q4","startdate":"2017-10-01","enddate":"2017-12-31","processingscheduleid":"057921bd-1841-4748-8523-dbe5ebb58368"},{"id":"baf98c9a-408b-4e9e-b56a-38640c7dfed6","name":"Period 008","startdate":"2017-08-09","enddate":"2017-09-09","processingscheduleid":"d036a3bc-865a-402b-972e-96e0695bf8ce"},{"id":"a4eefb4e-2bb6-4793-9f70-2cc5365de7c6","name":"Period 009","startdate":"2017-06-10","enddate":"2017-09-09","processingscheduleid":"9fca3d5d-b2c8-46c8-adb0-96e38d2c1f6f"},{"id":"f2cecbc4-c6cc-48ca-b4e7-11e69238b386","name":"Period 010","startdate":"2017-09-10","enddate":"2017-10-10","processingscheduleid":"9fca3d5d-b2c8-46c8-adb0-96e38d2c1f6f"}]'));
INSERT INTO referencedata.product_categories (SELECT * FROM json_populate_recordset(NULL::referencedata.product_categories, '[{"id":"6805dd74-ec00-4697-9a5d-5b78414fcaf6","code":"C1","displayname":"Antibiotics","displayorder":1},{"id":"814c51b6-91e3-45c3-af39-25e94b608d96","code":"C2","displayname":"Anaesthetics","displayorder":2},{"id":"baeb2434-574d-4804-9640-992abbe826ef","code":"C3","displayname":"Anti-fungal","displayorder":3},{"id":"6c7768ef-6ccc-4150-a589-043a17f6cf95","code":"C4","displayname":"Analgesics","displayorder":4},{"id":"16173fd0-f439-4222-931e-91c413a495c3","code":"C5","displayname":"Vaccines","displayorder":5},{"id":"15b8ef1f-a5d6-42dd-95bf-bb68a4504e82","code":"oc","displayname":"Oral Contraceptive","displayorder":6},{"id":"3bfe0282-732b-4ce8-b4f9-aa7ffb297758","code":"bm","displayname":"Barrier Method","displayorder":7},{"id":"06606981-67cc-42d1-87ed-21f22fb0ea58","code":"imp","displayname":"Implantable Contraceptive","displayorder":8},{"id":"74f39ab7-b5ce-4087-a6ea-65799a35e79b","code":"inj","displayname":"Injectable Hormonal Contraceptive","displayorder":9}]'));
INSERT INTO referencedata.orderable_products (SELECT * FROM json_populate_recordset(NULL::referencedata.orderable_products, '[{"id":"2400e410-b8dd-4954-b1c0-80d8a8e785fc","code":"C1","name":"Acetylsalicylic Acid","type":"TRADE_ITEM","dispensingunit":"10 tab strip","packsize":16,"packroundingthreshold":8,"roundtozero":false,"programproducts":[{"id":"9e25fd93-d8a0-4d1d-8188-55dd3c408d24"}]},{"id":"103396e1-f5e4-4754-bb7b-1c799097dfea","code":"C2","name":"Glibenclamide","type":"TRADE_ITEM","dispensingunit":"each","packsize":40,"packroundingthreshold":20,"roundtozero":false,"programproducts":[{"id":"d4e4c6ca-efc9-4ab4-8966-d78ba594ac0c"}]},{"id":"bac90734-6e58-426e-849b-331286ec77d2","code":"C3","name":"Streptococcus Pneumoniae Vaccine","type":"TRADE_ITEM","dispensingunit":"10 tab strip","packsize":5,"packroundingthreshold":2,"roundtozero":false,"programproducts":[{"id":"267321d1-447c-4fac-9430-907ae38d1854"}]},{"id":"c9e65f02-f84f-4ba2-85f7-e2cb6f0989af","code":"C4","name":"Streptococcus Pneumoniae Vaccine II","type":"GLOBAL_PRODUCT","dispensingunit":"each","packsize":5,"packroundingthreshold":2,"roundtozero":false,"programproducts":[{"id":"1af3b535-fe6a-41ce-b4c6-7d0aa4381cdb"}]},{"id":"d602d0c6-4052-456c-8ccd-61b4ad77bece","code":"C100","name":"Levora","type":"GLOBAL_PRODUCT","dispensingunit":"10 tab strip","packsize":84,"packroundingthreshold":42,"roundtozero":false,"programproducts":[{"id":"10d447ea-0a86-4267-8048-0b331d509601"}]},{"id":"23819693-0670-4c4b-b400-28e009b86b51","code":"C200","name":"Ortho-Novum","type":"GLOBAL_PRODUCT","dispensingunit":"each","packsize":63,"packroundingthreshold":31,"roundtozero":false,"programproducts":[{"id":"2e9cf7eb-ae09-431b-a3da-aa51a7f4aca0"}]},{"id":"7f7b83db-580d-4269-88d2-7f9c80a591a9","code":"C300","name":"Depo-Estradiol","type":"GLOBAL_PRODUCT","dispensingunit":"each","packsize":5,"packroundingthreshold":2,"roundtozero":false,"programproducts":[{"id":"366fdbed-c1ba-4bd0-b387-b1c75df9da48"}]},{"id":"bfde33c3-83f1-4feb-a27c-842852f66b71","code":"C400","name":"Male Condom","type":"GLOBAL_PRODUCT","dispensingunit":"each","packsize":1,"packroundingthreshold":0,"roundtozero":false,"programproducts":[{"id":"e119197f-2326-47b1-9a7f-00f020d9e2b9"}]},{"id":"880cf2eb-7b68-4450-a037-a0dec1a17987","code":"C500","name":"Implanon","type":"GLOBAL_PRODUCT","dispensingunit":"10 tab strip","packsize":1,"packroundingthreshold":0,"roundtozero":false,"programproducts":[{"id":"ceaa00b6-e2bb-4908-ab82-4e2265c39404"}]},{"id":"7b9b66e0-f008-4c38-93ec-7a5f4868cca9","code":"C600","name":"Levonorgestrel ","type":"GLOBAL_PRODUCT","dispensingunit":"10 tab strip","packsize":1,"packroundingthreshold":0,"roundtozero":false,"programproducts":[{"id":"79159377-dc1c-4eaa-b4e9-8dad221a1a97"}]}]'));
INSERT INTO referencedata.programs (SELECT * FROM json_populate_recordset(NULL::referencedata.programs, '[{"id":"dce17f2e-af3e-40ad-8e00-3496adef44c3","code":"PRG001","name":"Family Planning","active":"true","periodsskippable":"true","shownonfullsupplytab":"true"},{"id":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","code":"PRG002","name":"Essential Meds","active":"true","periodsskippable":"false"},{"id":"66032ea8-b69b-4102-a1eb-844e57143187","code":"PRG003","name":"New program","active":"true","periodsskippable":"false"}]'));
INSERT INTO referencedata.program_products (SELECT * FROM json_populate_recordset(NULL::referencedata.program_products, '[{"id":"9e25fd93-d8a0-4d1d-8188-55dd3c408d24","active":"true","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","productid":"2400e410-b8dd-4954-b1c0-80d8a8e785fc","productcategoryid":"6805dd74-ec00-4697-9a5d-5b78414fcaf6","dosespermonth":10,"fullsupply":"true","maxmonthsstock":1000,"displayorder":1,"priceperpack":3.155},{"id":"d4e4c6ca-efc9-4ab4-8966-d78ba594ac0c","active":"false","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","productid":"103396e1-f5e4-4754-bb7b-1c799097dfea","productcategoryid":"6805dd74-ec00-4697-9a5d-5b78414fcaf6","dosespermonth":3,"fullsupply":"true","maxmonthsstock":10,"displayorder":2,"priceperpack":2.134},{"id":"267321d1-447c-4fac-9430-907ae38d1854","active":"true","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","productid":"bac90734-6e58-426e-849b-331286ec77d2","productcategoryid":"16173fd0-f439-4222-931e-91c413a495c3","dosespermonth":1,"fullsupply":"false","maxmonthsstock":30,"displayorder":3,"priceperpack":5.153},{"id":"1af3b535-fe6a-41ce-b4c6-7d0aa4381cdb","active":"true","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","productid":"c9e65f02-f84f-4ba2-85f7-e2cb6f0989af","productcategoryid":"16173fd0-f439-4222-931e-91c413a495c3","dosespermonth":1,"fullsupply":"true","maxmonthsstock":30,"displayorder":4,"priceperpack":4.1344},{"id":"10d447ea-0a86-4267-8048-0b331d509601","active":"true","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","productid":"d602d0c6-4052-456c-8ccd-61b4ad77bece","productcategoryid":"15b8ef1f-a5d6-42dd-95bf-bb68a4504e82","dosespermonth":1,"fullsupply":"true","maxmonthsstock":1000,"displayorder":1},{"id":"2e9cf7eb-ae09-431b-a3da-aa51a7f4aca0","active":"true","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","productid":"23819693-0670-4c4b-b400-28e009b86b51","productcategoryid":"15b8ef1f-a5d6-42dd-95bf-bb68a4504e82","dosespermonth":1,"fullsupply":"true","maxmonthsstock":1000,"displayorder":2,"priceperpack":15.9394},{"id":"366fdbed-c1ba-4bd0-b387-b1c75df9da48","active":"true","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","productid":"7f7b83db-580d-4269-88d2-7f9c80a591a9","productcategoryid":"74f39ab7-b5ce-4087-a6ea-65799a35e79b","dosespermonth":1,"fullsupply":"false","maxmonthsstock":20,"displayorder":6,"priceperpack":20.774},{"id":"e119197f-2326-47b1-9a7f-00f020d9e2b9","active":"true","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","productid":"bfde33c3-83f1-4feb-a27c-842852f66b71","productcategoryid":"3bfe0282-732b-4ce8-b4f9-aa7ffb297758","dosespermonth":1,"fullsupply":"true","maxmonthsstock":20,"displayorder":3,"priceperpack":25.464},{"id":"ceaa00b6-e2bb-4908-ab82-4e2265c39404","active":"true","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","productid":"880cf2eb-7b68-4450-a037-a0dec1a17987","productcategoryid":"06606981-67cc-42d1-87ed-21f22fb0ea58","dosespermonth":1,"fullsupply":"false","maxmonthsstock":20,"displayorder":4,"priceperpack":30.9831},{"id":"79159377-dc1c-4eaa-b4e9-8dad221a1a97","active":"true","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","productid":"7b9b66e0-f008-4c38-93ec-7a5f4868cca9","productcategoryid":"06606981-67cc-42d1-87ed-21f22fb0ea58","dosespermonth":1,"fullsupply":"true","maxmonthsstock":20,"displayorder":5,"priceperpack":5.1},{"id":"99b2a62a-f96d-45ad-ba91-7dcc6cda7ce3","active":"true","programid":"66032ea8-b69b-4102-a1eb-844e57143187","productid":"7f7b83db-580d-4269-88d2-7f9c80a591a9","productcategoryid":"74f39ab7-b5ce-4087-a6ea-65799a35e79b","dosespermonth":1,"fullsupply":"true","maxmonthsstock":20,"displayorder":6,"priceperpack":4.34}]'));
INSERT INTO referencedata.stock_adjustment_reasons (SELECT * FROM json_populate_recordset(NULL::referencedata.stock_adjustment_reasons, '[{"id":"9e25fd93-d8a0-4d1d-8188-55dd3c408d24","name":"Transfer In","description":"Transfer In","additive":true,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":1},{"id":"236c97a5-1829-4304-b5b4-f40b3d11a136","name":"Transfer Out","description":"Transfer Out","additive":false,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":2},{"id":"71ee1dc3-5444-4a40-8b17-5676152e2ee1","name":"Damaged","description":"Damaged","additive":false,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":3},{"id":"3f671afa-9c28-4de0-b94e-6475e7ae15d4","name":"Lost","description":"Lost","additive":false,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":4},{"id":"85a6f1eb-853c-4283-921c-7125ef65b089","name":"Stolen","description":"Stolen","additive":false,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":5},{"id":"faf5b4c4-817c-4c2a-b9fb-ea3ac6094a17","name":"Expired","description":"Expired","additive":false,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":6},{"id":"212a775a-1632-4cf4-9701-99c568094b10","name":"Passed Open-Vial Time Limit","description":"Passed Open-Vial Time Limit","additive":false,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":7},{"id":"9beb151f-1162-4695-87e6-812552858336","name":"Cold Chain Failure","description":"Cold Chain Failure","additive":false,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":8},{"id":"bbc1d3fc-bd9d-4623-a10c-7f253bdc6199","name":"Clinic Return","description":"Clinic Return","additive":true,"programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","displayorder":9},{"id":"f1b833e0-3cad-4dc1-a13d-7d8b293c3b09","name":"Transfer In","description":"Transfer In","additive":true,"programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","displayorder":1},{"id":"b047c538-78a1-490a-86ec-9fe5256943b1","name":"Transfer Out","description":"Transfer Out","additive":false,"programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","displayorder":2},{"id":"28fa945c-ff9c-4121-affb-5c9ac572e8e4","name":"Damaged","description":"Damaged","additive":false,"programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","displayorder":3},{"id":"cdbce4e1-70c1-4762-9d61-9e1577c7414c","name":"Lost","description":"Lost","additive":false,"programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","displayorder":4},{"id":"ed73fe63-2151-4dcb-83bc-461140b8e5a5","name":"Stolen","description":"Stolen","additive":false,"programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","displayorder":5},{"id":"038a6ede-8506-4438-9d4a-d012bbc74f36","name":"Expired","description":"Expired","additive":false,"programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","displayorder":6},{"id":"881a5fff-b4cd-4d7f-9aac-e76bef6dbbf9","name":"Clinic Return","description":"Clinic Return","additive":true,"programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","displayorder":9},{"id":"18e3b832-671c-493e-8359-c2133be5288c","name":"Transfer In","description":"Transfer In","additive":true,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":1},{"id":"530c5c76-cdf0-4d6e-b3a4-f599f73f47fd","name":"Transfer Out","description":"Transfer Out","additive":false,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":2},{"id":"2ae965c6-2218-4f71-88f2-d0ad1c5af7e2","name":"Damaged","description":"Damaged","additive":false,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":3},{"id":"61f65c5b-2e29-4657-9905-459898d85426","name":"Lost","description":"Lost","additive":false,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":4},{"id":"8dab137b-4aff-4884-90ef-5bc7fc25fdba","name":"Stolen","description":"Stolen","additive":false,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":5},{"id":"cef4ff73-1a42-4481-98d1-57092c9d5fad","name":"Expired","description":"Expired","additive":false,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":6},{"id":"30f6002d-a78d-44db-b2b9-46e76795a188","name":"Passed Open-Vial Time Limit","description":"Passed Open-Vial Time Limit","additive":false,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":7},{"id":"6f8d0431-6ec5-4280-9019-c6024b168b23","name":"Cold Chain Failure","description":"Cold Chain Failure","additive":false,"programid":"66032ea8-b69b-4102-a1eb-844e57143187","displayorder":8}]'));
INSERT INTO referencedata.supply_lines (SELECT * FROM json_populate_recordset(NULL::referencedata.supply_lines, '[{"id":"54e16aae-35f1-489a-9819-0a677de50d19","description":"Family Planning","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","supplyingfacilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e"},{"id":"dd343fe4-ec0c-4a58-831a-68ccf9f19316","description":"Essential Meds","supervisorynodeid":"6d5831e7-c336-4129-bd10-0149910a72aa","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","supplyingfacilityid":"44a29ccb-ed55-4c26-9eca-7e5759269b25"}]'));
INSERT INTO referencedata.users (SELECT * FROM json_populate_recordset(NULL::referencedata.users, '[{"id":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","username":"administrator","email":"administrator@openlmis.org","verified":"true","active":"true","homefacilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","firstname":"Admin","lastname":"Admin","loginrestricted":"false","timezone":"CET"},{"id":"56a50e9a-9668-437d-a09c-7e709ce22222","username":"devadmin","email":"devadmin@openlmis.org","verified":"true","active":"true","homefacilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","firstname":"Admin","lastname":"Admin","loginrestricted":"false","timezone":"CET"},{"id":"534e7c8a-b316-11e6-80f5-76304dec7eb7","username":"StoreInCharge","email":"testOpenLMIS@gmail.com","verified":"true","active":"true","homefacilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","firstname":"Fatima","lastname":"Doe","loginrestricted":"false","timezone":"CET"}]'));
INSERT INTO referencedata.roles (SELECT * FROM json_populate_recordset(NULL::referencedata.roles, '[{"id":"185db8f7-ee35-44d0-8b40-6de12489ae77","name":"Storeroom Manager"},{"id":"d30a2fbb-51d9-49ad-90fe-fb405470a439","name":"Program Supervisor"},{"id":"e16d802b-8eb1-43f7-adf8-ef123227cb88","name":"Warehouse Clerk"},{"id":"a439c5de-b8aa-11e6-80f5-76304dec7eb7","name":"Admin"},{"id":"2a2eaeba-bb9a-11e6-a4a6-cec0c932ce01","name":"Program Coordinator"}]'));
INSERT INTO referencedata.role_assignments (SELECT * FROM json_populate_recordset(NULL::referencedata.role_assignments, '[{"id":"8ad78ea5-aa02-4b69-bb27-400e5e1d395b","type":"supervision","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","userid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"6745e82c-99df-11e6-9f33-a24fc0d9649c","type":"supervision","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","userid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"d9af4645-4c77-4360-94fc-32a514b9268c","type":"supervision","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","userid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"88972916-329f-4552-94c0-c141d02945d2","type":"supervision","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","userid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"00679f16-d498-447e-8c75-a6a7ab220fb4","type":"supervision","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","userid":"56a50e9a-9668-437d-a09c-7e709ce22222","roleid":"2a2eaeba-bb9a-11e6-a4a6-cec0c932ce01"},{"id":"75f5dc44-e137-41bc-bf71-d5bbe680ef84","type":"supervision","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","userid":"56a50e9a-9668-437d-a09c-7e709ce22222","roleid":"2a2eaeba-bb9a-11e6-a4a6-cec0c932ce01"},{"id":"865f67cc-c3c0-4d74-89b5-fa16ec2e17b9","type":"supervision","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","userid":"56a50e9a-9668-437d-a09c-7e709ce22222","roleid":"2a2eaeba-bb9a-11e6-a4a6-cec0c932ce01"},{"id":"a2f8fca1-2bc3-495a-8629-4758a7a08095","type":"supervision","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","userid":"56a50e9a-9668-437d-a09c-7e709ce22222","roleid":"2a2eaeba-bb9a-11e6-a4a6-cec0c932ce01"},{"id":"4b3c6622-48cb-4c1b-9a79-f37243c37063","type":"fulfillment","warehouseid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","userid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","roleid":"185db8f7-ee35-44d0-8b40-6de12489ae77"},{"id":"27c94f92-b8aa-11e6-80f5-76304dec7eb7","type":"direct","userid":"a337ec45-31a0-4f2b-9b2e-a105c4b669bb","roleid":"a439c5de-b8aa-11e6-80f5-76304dec7eb7"},{"id":"4b99e7c5-5e7e-4c17-b196-d178214f6046","type":"supervision","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","userid":"534e7c8a-b316-11e6-80f5-76304dec7eb7","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"5034d51d-1fa3-45f8-8373-75009d90b089","type":"supervision","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","userid":"534e7c8a-b316-11e6-80f5-76304dec7eb7","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"2efda65e-d11a-438d-b265-31a3580145a2","type":"supervision","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","userid":"534e7c8a-b316-11e6-80f5-76304dec7eb7","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"c5a03d3c-e019-4266-8bba-faf7c7f4b839","type":"supervision","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","userid":"534e7c8a-b316-11e6-80f5-76304dec7eb7","roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439"},{"id":"3c995a11-29d2-4d99-8edc-2d5f5c991e0e","type":"fulfillment","warehouseid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","userid":"534e7c8a-b316-11e6-80f5-76304dec7eb7","roleid":"185db8f7-ee35-44d0-8b40-6de12489ae77"},{"id":"ef5dbd57-069f-470e-80b0-7ebc14fff9ab","type":"direct","userid":"534e7c8a-b316-11e6-80f5-76304dec7eb7","roleid":"a439c5de-b8aa-11e6-80f5-76304dec7eb7"}]'));
INSERT INTO referencedata.requisition_groups (SELECT * FROM json_populate_recordset(NULL::referencedata.requisition_groups, '[{"id":"1642bfd8-94e1-4fcf-80e7-254b29ca8e05","code":"RGFP","name":"RG Family Planning","supervisorynodeid":"fb38bd1c-beeb-4527-8345-900900329c10"},{"id":"84b33e28-f537-4a76-8895-9d4d564a623a","code":"RGES","name":"RG Essential Meds","supervisorynodeid":"9f470265-0770-4dd1-bd5a-cf8fe3734d79"},{"id":"e90da490-a371-4c2f-83c3-7c1dc9748d48","code":"RGNP","name":"RG New program","supervisorynodeid":"0f48d25f-2cc3-4f42-a58b-5c74aab70224"}]'));
INSERT INTO referencedata.requisition_group_program_schedules (SELECT * FROM json_populate_recordset(NULL::referencedata.requisition_group_program_schedules, '[{"id":"e7aab4ca-4586-4ef2-8923-dc3940bb3104","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3","processingscheduleid":"9c15bd6e-3f6b-4b91-b53a-36c199d35eac","directdelivery":"true","dropofffacilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","requisitiongroupid":"1642bfd8-94e1-4fcf-80e7-254b29ca8e05"},{"id":"56b50f88-13e2-4dd1-8210-fead7025b106","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a","processingscheduleid":"057921bd-1841-4748-8523-dbe5ebb58368","directdelivery":"false","dropofffacilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","requisitiongroupid":"84b33e28-f537-4a76-8895-9d4d564a623a"},{"id":"298da8ae-bff9-4f1c-99f5-49e02b2b3b91","programid":"66032ea8-b69b-4102-a1eb-844e57143187","processingscheduleid":"083775ab-b9af-41a3-b845-0fbf4fe1583d","directdelivery":"false","dropofffacilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","requisitiongroupid":"e90da490-a371-4c2f-83c3-7c1dc9748d48"}]'));
INSERT INTO referencedata.supported_programs (SELECT * FROM json_populate_recordset(NULL::referencedata.supported_programs, '[{"id":"29172001-809d-45bd-bbae-3a53b544709a","active":true,"facilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3"},{"id":"a3da399c-547e-4dbe-9b94-8361f626b334","active":true,"facilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a"},{"id":"56615552-9f08-4b2c-8553-562774fd4c94","active":true,"facilityid":"13037147-1769-4735-90a7-b9b310d128b8","programid":"dce17f2e-af3e-40ad-8e00-3496adef44c3"},{"id":"0f19e23e-207b-41f2-8519-200585197bb9","active":true,"facilityid":"13037147-1769-4735-90a7-b9b310d128b8","programid":"10845cb9-d365-4aaa-badd-b4fa39c6a26a"},{"id":"ceced192-fb51-41ee-94e3-b9369d849d28","active":true,"facilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e","programid":"66032ea8-b69b-4102-a1eb-844e57143187"}]'));
INSERT INTO referencedata.requisition_group_members (SELECT * FROM json_populate_recordset(NULL::referencedata.requisition_group_members, '[{"requisitiongroupid":"1642bfd8-94e1-4fcf-80e7-254b29ca8e05","facilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce"},{"requisitiongroupid":"1642bfd8-94e1-4fcf-80e7-254b29ca8e05","facilityid":"13037147-1769-4735-90a7-b9b310d128b8"},{"requisitiongroupid":"1642bfd8-94e1-4fcf-80e7-254b29ca8e05","facilityid":"ab95f1cf-ae8d-4888-ba9d-05141d3f50de"},{"requisitiongroupid":"1642bfd8-94e1-4fcf-80e7-254b29ca8e05","facilityid":"44a29ccb-ed55-4c26-9eca-7e5759269b25"},{"requisitiongroupid":"1642bfd8-94e1-4fcf-80e7-254b29ca8e05","facilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e"},{"requisitiongroupid":"84b33e28-f537-4a76-8895-9d4d564a623a","facilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce"},{"requisitiongroupid":"84b33e28-f537-4a76-8895-9d4d564a623a","facilityid":"13037147-1769-4735-90a7-b9b310d128b8"},{"requisitiongroupid":"84b33e28-f537-4a76-8895-9d4d564a623a","facilityid":"ab95f1cf-ae8d-4888-ba9d-05141d3f50de"},{"requisitiongroupid":"84b33e28-f537-4a76-8895-9d4d564a623a","facilityid":"44a29ccb-ed55-4c26-9eca-7e5759269b25"},{"requisitiongroupid":"84b33e28-f537-4a76-8895-9d4d564a623a","facilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e"},{"requisitiongroupid":"e90da490-a371-4c2f-83c3-7c1dc9748d48","facilityid":"e6799d64-d10d-4011-b8c2-0e4d4a3f65ce"},{"requisitiongroupid":"e90da490-a371-4c2f-83c3-7c1dc9748d48","facilityid":"13037147-1769-4735-90a7-b9b310d128b8"},{"requisitiongroupid":"e90da490-a371-4c2f-83c3-7c1dc9748d48","facilityid":"ab95f1cf-ae8d-4888-ba9d-05141d3f50de"},{"requisitiongroupid":"e90da490-a371-4c2f-83c3-7c1dc9748d48","facilityid":"44a29ccb-ed55-4c26-9eca-7e5759269b25"},{"requisitiongroupid":"e90da490-a371-4c2f-83c3-7c1dc9748d48","facilityid":"19121381-9f3d-4e77-b9e5-d3f59fc1639e"}]'));
INSERT INTO referencedata.role_rights (SELECT * FROM json_populate_recordset(NULL::referencedata.role_rights, '[{"roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439","rightid":"9ade922b-3523-4582-bef4-a47701f7df14"},{"roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439","rightid":"bffa2de2-dc2a-47dd-b126-6501748ac3fc"},{"roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439","rightid":"feb4c0b8-f6d2-4289-b29d-811c1d0b2863"},{"roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439","rightid":"c3eb5df0-c3ac-4e70-a978-02827462f60e"},{"roleid":"d30a2fbb-51d9-49ad-90fe-fb405470a439","rightid":"e101d2b8-6a0f-4af6-a5de-a9576b4ebc50"},{"roleid":"185db8f7-ee35-44d0-8b40-6de12489ae77","rightid":"7958129d-c4c0-4294-a40c-c2b07cb8e515"},{"roleid":"185db8f7-ee35-44d0-8b40-6de12489ae77","rightid":"65626c3d-513f-4255-93fd-808709860594"},{"roleid":"a439c5de-b8aa-11e6-80f5-76304dec7eb7","rightid":"e96017ff-af8c-4313-a070-caa70465c949"},{"roleid":"a439c5de-b8aa-11e6-80f5-76304dec7eb7","rightid":"8816edba-b8a9-11e6-80f5-76304dec7eb7"},{"roleid":"a439c5de-b8aa-11e6-80f5-76304dec7eb7","rightid":"4bed4f40-36b5-42a7-94c9-0fd3d4252374"},{"roleid":"2a2eaeba-bb9a-11e6-a4a6-cec0c932ce01","rightid":"e101d2b8-6a0f-4af6-a5de-a9576b4ebc50"},{"roleid":"e16d802b-8eb1-43f7-adf8-ef123227cb88","rightid":"e101d2b8-6a0f-4af6-a5de-a9576b4ebc50"}]'));
INSERT INTO referencedata.facility_type_approved_products (SELECT * FROM json_populate_recordset(NULL::referencedata.facility_type_approved_products, '[{"id":"c5d4427b-7427-4209-ae35-1d5bd92ef3b8","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"9e25fd93-d8a0-4d1d-8188-55dd3c408d24","maxmonthsofstock":"5.5","minmonthsofstock":"3.3","emergencyorderpoint":"6"},{"id":"f691c323-e152-459a-ba64-15c23e34dedd","facilitytypeid":"663b1d34-cc17-4d60-9619-e553e45aa441","programproductid":"9e25fd93-d8a0-4d1d-8188-55dd3c408d24","maxmonthsofstock":"12","minmonthsofstock":"8.5","emergencyorderpoint":"15"},{"id":"43f407db-29b8-4525-baaa-e1cd6844093f","facilitytypeid":"663b1d34-cc17-4d60-9619-e553e45aa441","programproductid":"267321d1-447c-4fac-9430-907ae38d1854","maxmonthsofstock":"18","minmonthsofstock":"12","emergencyorderpoint":"24"},{"id":"1125d62f-f9f3-40b8-8327-5204b64315ed","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"1af3b535-fe6a-41ce-b4c6-7d0aa4381cdb","maxmonthsofstock":"18","minmonthsofstock":"12","emergencyorderpoint":"24"},{"id":"b2ebdcbc-662c-4467-846a-af5f0068d1ca","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"10d447ea-0a86-4267-8048-0b331d509601","maxmonthsofstock":"1000","minmonthsofstock":"100","emergencyorderpoint":"75"},{"id":"7e407f1c-5681-4b80-8600-f97beec69a29","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"2e9cf7eb-ae09-431b-a3da-aa51a7f4aca0","maxmonthsofstock":"1000","minmonthsofstock":"100","emergencyorderpoint":"75"},{"id":"7be8822f-b87c-40b8-a2b3-4d0620e3cfe8","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"366fdbed-c1ba-4bd0-b387-b1c75df9da48","maxmonthsofstock":"500","minmonthsofstock":"50","emergencyorderpoint":"25"},{"id":"a7ef2398-052a-4e31-9a53-a5fbe702e39d","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"e119197f-2326-47b1-9a7f-00f020d9e2b9","maxmonthsofstock":"500","minmonthsofstock":"50","emergencyorderpoint":"25"},{"id":"34d820a9-37b9-44aa-90df-c736f8481f77","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"ceaa00b6-e2bb-4908-ab82-4e2265c39404","maxmonthsofstock":"500","minmonthsofstock":"50","emergencyorderpoint":"25"},{"id":"3c3ef7ad-2447-4643-b1bd-de9c0c159da6","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"79159377-dc1c-4eaa-b4e9-8dad221a1a97","maxmonthsofstock":"500","minmonthsofstock":"50","emergencyorderpoint":"25"},{"id":"ab0d91d4-0251-483e-ac98-ed7d1369dd27","facilitytypeid":"ac1d268b-ce10-455f-bf87-9c667da8f060","programproductid":"99b2a62a-f96d-45ad-ba91-7dcc6cda7ce3","maxmonthsofstock":"500","minmonthsofstock":"50","emergencyorderpoint":"25"}]'));