<h1>Storage (final project)</h1>

<p>Storage final project:</p>
<ol>
<li>Создать приложение для склада овощей</li>
<li>При создании использовать: Java 11, систему сборки (Maven), Git, реляционную базу данных.</li>
<li>Проект должен быть мультимодульный с пагинацией страниц (там где это необходимо) и использованием логера</li>
<li>Дополнительные требования: использование connection pool, библиотеки lombok, системы контроля базы данных и др.</li>
</ol>


<h2>What's done:</h2>
<ol>
<li>Создан мультимодульный проект используя Java 11, систему сборки (Maven), Git, реляционную базу данных</li>
<li>Добавлена пагинация страниц по адресу: /goods?page= и /storehouses?page= , добавлено логирование в сервис и web слое</li>
<li>Для хранения и получения данных используется база данных Postgresql, подключение получаем из Hikary connection pool.</li>
<li>Версионирование базы данных производится с помощью liquibase.</li>
<li>Приложение разделено на 4 модульных слоя для сущностей, dao, сервисов и контроллеров.</li>
<li>Приложение построено по концепции REST-сервиса, данные принимаются и отправляются в json-формате</li>
<li>С помощью приложения можно добвалять: категории товаров, сами товары (их характеристики и описание), добавлять товары с ценой и количеством на склад, резервировать и покупать товары со склада.</li>
<li>Для авторизации и разделения ролей используется spring security.</li>
<li>Существуествует несколько ролей пользователей admin и user. Admin может сохранять, удалять, обновлять категории, товары, информацию о товаре на складе. Просматривать отчет о купленных товарах.</li>
<li>User может просматривать информацию о имеющихся товарах на складе, резервировать, покупать и удалять товар из зарезервированных.</li>
<li>Для отображения информации пользователю используются DTO-объекты, для мапинга используется библиотека mapstruct. Валидация DTO объектов происходит на уровне контроллера.</li>
</ol>

<h2>Project's limitations</h2>
<ol>
<li>Отсутствие использования не реляционной базы данных</li>
<li>другое...</li>
</ol>

<h3>To run App you should:</h3>
<ol>
<li>Run docker daemon</li>
<li>Build project: $mvn clean install</li>
<li>Run new postgresql server for the App: $docker-compose up -d</li>
<li>Run liquibase to create tables and insert data:</li>
	<ol>
		<li>$cd storage-persistence</li>
		<li>$mvn liquibase:update</li>
	</ol>
<li>Run App on server:</li>
	<ol>
		<li>should login with post request including json-data { "username" : "Admin", "password" : "admin" } or json-data { "username" : "User", "password" : "user" } mapped on /storage/login</li>
		<li>showing list of goods map to /storage/goods and etc</li>
	</ol>
</ol>