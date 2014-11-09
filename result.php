<!DOCTYPE html>

<html>
  <head>
    <title>Twitter</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  	<link rel="stylesheet" type="text/css" href="bootstrap-min.css" />
  	<link rel="stylesheet" type="text/css" href="super.css" />

     <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">

      // Load the Visualization API and the piechart package.
      google.load('visualization', '1.0', {'packages':['corechart']});

      // Set a callback to run when the Google Visualization API is loaded.
      google.setOnLoadCallback(drawChart);

      // Callback that creates and populates a data table,
      // instantiates the pie chart, passes in the data and
      // draws it.
      function drawChart() {

        // Create the data table.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        data.addRows([
          ['Mushrooms', 3],
          ['Onions', 1],
          ['Olives', 1],
          ['Zucchini', 1],
          ['Pepperoni', 2]
        ]);

        // Set chart options
        var options = {'title':'How Much Pizza I Ate Last Night',
                       'width':400,
                       'height':300};

        // Instantiate and draw our chart, passing in some options.
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>

  </head>
  <body>

  	<div id="Top">
  		<h1 align="center"> Twitter Feelings <h1>
  	</div>

  	<div id="container">
      <div id="chart_div"></div>

      <form action="index.html">
    <input type="submit" class="btn btn-primary center" value="New Search">
</form> 
  		
  	</div>

  	<div id="foot">
 	</div>


    
    <script src="http://code.jquery.com/jquery.js"></script>
  </body>
</html>