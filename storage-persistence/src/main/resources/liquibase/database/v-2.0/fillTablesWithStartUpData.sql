Insert Into categories (name, description) values ('фрукты', 'Фрукт — сочный съедобный плод растения. Пример: яблоко.');
Insert Into categories (name, description) values ('овощи', 'Пример: Картофельк морковь, свекла и т.д.');
Insert Into categories (name, description) values ('зелень', 'Пример: петрушка, укроп, зеленый лук и т.д.');
Insert Into categories (name, description) values ('ягоды', 'Пример: брусника, клубника, смародина и т.д.');


Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО "Ягода-малина"', '212030', 'Республика Беларусь', 'Минская обл.', 'деревня Гудки', 'ул. Красная', '6');
Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО "Плодоовощное"', '220265', 'Республика Беларусь', 'Минская обл.', 'д. Верба', 'ул. Синяя', '18');
Insert Into producers(name, postal_code, country, region, locality) values('ООО "Урожай"', '245879', 'Республика Беларусь', 'Минская обл.', 'д. Весна');
Insert Into producers(name, postal_code, country, region, locality) values('ОАО "Хутор"', '215365', 'Республика Беларусь', 'Минская обл.', 'д. Солнцево');
Insert Into producers(name, postal_code, country, region, locality) values('ОАО "Фермер"', '210086', 'Республика Беларусь', 'Минская обл.', 'д. Кремлевская');
Insert Into producers(name, postal_code, country, region, locality) values('ОДО "Колхозница и рабочий"', '250245', 'Республика Беларусь', 'Витебскаяя обл.', 'д. Антоновка');
Insert Into producers(name, postal_code, country, region, locality) values('ОДО "Красный товарищ"', '212569', 'Республика Беларусь', 'Гродненская обл.', 'д. Хутор на земле');
Insert Into producers(name, postal_code, country, region, locality) values('ОДО "Сто пудов"', '290452', 'Республика Беларусь', 'Гродненская обл.', 'д. Явный вес');
Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО "Зеленый мир"', '230985', 'Республика Беларусь', 'Витебская обл.', 'д. Ванильные горы', 'ул. Горняков', 'д. 3, оф. 25');
Insert Into producers(name, postal_code, country, region, locality, street, building) values('СООО "Урожайница"', '230564', 'Республика Беларусь', 'Витебская обл.', 'д. Светлый путь', 'ул. Конева', 'д. 2');
Insert Into producers(name, postal_code, country, region, locality, street, building) values('ООО "Растеневоды"', '212045', 'Республика Беларусь', 'Могилевская обл.', 'д. Красные горы', 'ул. Скворцово', 'д. 21, оф. 5');
Insert Into producers(name, postal_code, country, region, locality) values('ООО "Зеленый мир"', '220032', 'Республика Беларусь', 'Могилевская обл.', 'д. Мухоморы');


Insert Into goods(name, sort, description, category_id, producer_id) values('Яблоко', 'Черный принц', 'Яблоко для производства сока', 1, 3);
Insert Into goods(name, sort, description, category_id, producer_id) values('Яблоко', 'Карамельлка', 'Яблоко для детей', 1, 5);
Insert Into goods(name, sort, description, category_id, producer_id) values('Яблоко', 'Голден', 'Яблоко без косточек', 1, 8);
Insert Into goods(name, sort, description, category_id, producer_id) values('Груша', 'Видная', 'Груша без косточек', 1, 4);
Insert Into goods(name, sort, description, category_id, producer_id) values('Груша', 'Просто Мария', 'Груша для варенья', 1, 3);
Insert Into goods(name, sort, description, category_id, producer_id) values('Груша', 'Банановая', 'Груша для детей', 1, 6);
Insert Into goods(name, sort, description, category_id, producer_id) values('Груша', 'Дюймовочка', 'Груша для производства сока', 1, 5);
Insert Into goods(name, sort, description, category_id, producer_id) values('Персик', 'Киевский ранний', 'Персик необычный своим составом и вкусом', 1, 12);
Insert Into goods(name, sort, description, category_id, producer_id) values('Персик', 'Саратовский ранний', 'Персик на любителя с кислинкой', 1, 10);
Insert Into goods(name, sort, description, category_id, producer_id) values('Персик', 'Харроу Даймонд', 'Персик очень сладкий как мед', 1, 5);
Insert Into goods(name, sort, description, category_id, producer_id) values('Персик', 'Новоселковский', 'Персик мелкого размера', 1, 1);
Insert Into goods(name, sort, description, category_id, producer_id) values('Банан', 'Зеленый', 'Банан сочный без особого вкуса', 1, 9);
Insert Into goods(name, sort, description, category_id, producer_id) values('Банан', 'Красный', 'Банан крупный сладкий', 1, 11);
Insert Into goods(name, sort, description, category_id, producer_id) values('Банан', 'Медовый', 'Банан мелкий сладкий и сочный', 1, 8);
Insert Into goods(name, sort, description, category_id, producer_id) values('Банан', 'Пуван', 'Банан заморский, подходит для сушки', 1, 4);

Insert Into goods(name, sort, description, category_id, producer_id) values('Картофель', 'Ред Скарлет', 'Картофель для драников', 2, 5);
Insert Into goods(name, sort, description, category_id, producer_id) values('Картофель', 'Жуковский', 'Картофель для варки', 2, 1);
Insert Into goods(name, sort, description, category_id, producer_id) values('Картофель', 'Адретта', 'Картофель для жарки', 2, 2);
Insert Into goods(name, sort, description, category_id, producer_id) values('Картофель', 'Беллароза', 'Картофель с повышенным содержанием крахмала', 2, 4);
Insert Into goods(name, sort, description, category_id, producer_id) values('Морковь', 'Тушон', 'Морковь крупная сочная для салта', 2, 4);
Insert Into goods(name, sort, description, category_id, producer_id) values('Морковь', 'Витаминная, 6', 'Морковь для производства сока', 2, 8);
Insert Into goods(name, sort, description, category_id, producer_id) values('Морковь', 'Московская зимняя А 515', 'Морковь для хранения на зиму', 2, 5);
Insert Into goods(name, sort, description, category_id, producer_id) values('Лук репчатый', 'Юконт', 'Лук горький ядреный', 2, 11);
Insert Into goods(name, sort, description, category_id, producer_id) values('Лук репчатый', 'Сноуболл', 'Лук для салата', 2, 1);
Insert Into goods(name, sort, description, category_id, producer_id) values('Лук репчатый', 'Сноуболл', 'Лук для варки', 2, 3);

Insert Into goods(name, sort, description, category_id, producer_id) values('Укроп', 'Редут', 'Укроп для детей', 3, 2);
Insert Into goods(name, sort, description, category_id, producer_id) values('Укроп', 'Зонтик', 'Укроп для салата', 3, 1);
Insert Into goods(name, sort, description, category_id, producer_id) values('Укроп', 'Амазон', 'Укроп для сушки и замораживания', 3, 7);
Insert Into goods(name, sort, description, category_id, producer_id) values('Петрушка', 'Кучерявец', 'Петрушка для салата', 3, 6);
Insert Into goods(name, sort, description, category_id, producer_id) values('Петрушка', 'Сахарная', 'Петрушка для сушки и замораживания', 3, 2);
Insert Into goods(name, sort, description, category_id, producer_id) values('Петрушка', 'Алба', 'Петрушка для супа', 3, 4);
Insert Into goods(name, sort, description, category_id, producer_id) values('Лук зеленый', 'Порей', 'Лук для супа', 3, 12);
Insert Into goods(name, sort, description, category_id, producer_id) values('Лук зеленый', 'Слизун', 'Лук для салата', 3, 9);
Insert Into goods(name, sort, description, category_id, producer_id) values('Лук зеленый', 'Батун', 'Лук очень жгучий', 3, 10);
Insert Into goods(name, sort, description, category_id, producer_id) values('Лук зеленый', 'Шнитт', 'Лук с толстым стеблем жгучий', 3, 11);

Insert Into goods(name, sort, description, category_id, producer_id) values('Малина', 'Глория', 'Малина ароматная с кислинкой', 4, 1);
Insert Into goods(name, sort, description, category_id, producer_id) values('Малина', 'Золотой гигант', 'Малина ароматная сладкая', 4, 2);
Insert Into goods(name, sort, description, category_id, producer_id) values('Малина', 'Ранний сюрприз', 'Малина для производства сока', 4, 5);
Insert Into goods(name, sort, description, category_id, producer_id) values('Клубника', 'Кент', 'Клубника для производства варенья', 4, 5);
Insert Into goods(name, sort, description, category_id, producer_id) values('Клубника', 'Великан', 'Клубника для производства сока', 4, 8);
Insert Into goods(name, sort, description, category_id, producer_id) values('Клубника', 'Садовая', 'Клубника сладкая средняя', 4, 3);


Insert INTO storehouse(good_id, ttn_num, quantity, price) values(1, '135698', 323, 4.2);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(5, '185697', 408, 6.7);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(9, '135698', 255, 7.2);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(13, '135698', 358, 4.4);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(15, '128652', 215, 5.1);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(18, '223563', 325, 3.3);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(20, '000254', 423, 5.3);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(23, '000256', 378, 4.9);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(27, '022569', 213, 8.2);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(32, 'Н256985', 161, 9.1);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(34, '023658', 135, 5.3);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(37, '000002', 128, 9.3);
Insert INTO storehouse(good_id, ttn_num, quantity, price) values(40, '000256', 118, 8.3);