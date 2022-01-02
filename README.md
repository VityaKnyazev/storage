<h1>Storage (final project)</h1>

<p>Home task lecture 21:</p>
<ol>
<li></li>
<li></li>
<li></li>
<li></li>
<li></li>
</ol>


<h2>What's done:</h2>
<ol>
<li></li>
<li></li>
<li></li>
<li></li>
<li></li>
<li></li>
</ol>

<h3>To run App you should:</h3>
<ol>
<li>Build project: $mvn clean install</li>
<li>Run new postgresql server for the App: $docker-compose up -d</li>
<li>Run liquibase to create tables and insert data:</li>
	<ol>
		<li>$cd storage-persistence</li>
		<li>$mvn liquibase:update</li>
	</ol>
<li>Run App on server mapped on /goods</li>
</ol>