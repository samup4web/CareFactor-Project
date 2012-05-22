<html>
  <head>
    <title>Stub view</title>
    <link rel="stylesheet" type="text/css" href="<?php echo base_url() . "css/style.css"; ?>" />
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.js"></script>
    <script type="text/javascript">
     
      $(document).ready(function(){ 
        $("#mobile_dummy_form").submit(function(event) {
          
          event.preventDefault(); 
                   
          method = $('#method').val(); 
          post_body = $('#post_body').val();
          request_url= $('#request_url').val();
          
          param_value = {
            "sensor_id" : method            
          };     
          
              
          url = '<?php echo base_url(); ?>'+"api/request/"+request_url;                   
          
          console.log(url);
          
          $.ajax({
            type: method,
            url: url ,
            data: post_body,
            //dataType: "xml",
            error: function(request, error){
              var content = $( request.responseText ).find( 'message' );
              $( "#display_screen_" ).empty().append("<span style=\"color:red\">input data invalid</span>");
              $( "#display_screen_" ).append( "<br><br>"+request.responseText );
              console.log(request.responseText);
            },
            success: function(request){
              //var content = $( request ).find( 'message' );
              //$( "#display_screen_" ).empty().append( request );
              
              if(url.indexOf("xml")!= -1){
                
                $("#display_screen_").empty().append("<table border=\"1\" id=\"display_screen_text\">"+
                  " <tr>"+
                  "<th>sensor_id</th>"+
                  "<th>gateway_id</th>"+
                  "<th>lon</th>"+
                  "<th>lat</th>"+
                  "<th>data_value</th>"+
                  "<th>time stamp</th>"+
                  "<th>inserted time</th>"+
                  " </tr>"+
                  "</table>");
                
                $(request).find('measurement').each(function(){
                  var sensor_id = $(this).find('sensor_id').text();
                  var gateway_id = $(this).find('gateway_id').text();
                  var pos_longitude = $(this).find('pos_longitude').text();
                  var pos_latitude = $(this).find('pos_latitude').text();
                  var data_value = $(this).find('data_value').text();
                  var timestamp = $(this).find('timestamp').text(); 
                  var update_time = $(this).find('update_time').text();
                                                
                
                                                
                  $("#display_screen_text").append(
                  "<tr>"+
                    "<td>"+ sensor_id +"</td>"+
                    "<td>"+ gateway_id+"</td>"+
                    "<td>"+ pos_longitude+"</td>"+
                    "<td>"+pos_latitude +"</td>"+
                    "<td>"+ data_value+"</td>"+
                    " <td>"+timestamp +"</td>"+
                    " <td>"+update_time +"</td>"+
                    "</tr>");                                
                });
                
                
              }else{
                
                $('#display_screen_').empty().append(request);
//                $('#display_screen_').empty().append("<textarea rows=\"300\" cols=\"400\">"+
//                request+
//                  "</textarea>" );
//                console.log("json");
              }
                                
              
            }
          });   
        });                  
      });  
     
    </script>
  </head>

  <body>
    <h1>End-User View</h1>
    <hr></br>


    <div id ='main'>
      <div>

        <?php
        $attributes = array('id' => 'mobile_dummy_form');
        echo form_open('', $attributes);

        echo form_fieldset('Mobile User Input Form');

        echo form_label("Http Method", 'method_');
        $options = array(
            'get' => 'GET'
//            'post' => 'POST',
//            'put' => 'PUT',
//            'delete' => 'DELETE',
        );
        //$shirts_on_sale = array('small', 'large');
        echo form_dropdown('method', $options, 'get', 'id="method"') . "<br>";


        echo form_label(base_url() . "api/request/", 'base_url');
        $data = array(
            'name' => 'request',
            'id' => 'request_url',
            'value' => 'all_measurements',
            'maxlength' => '100',
            'size' => '50',
            'style' => 'width:50%',
        );
        echo form_input($data) . "<br>";


//        echo form_label("POST request body", 'post_body');
//        $data = array(
//            'name' => 'request',
//            'id' => 'post_body',
//            'value' => '',
//            'maxlength' => '100',
//            'size' => '100',
//            'style' => 'width:74%',
//        );
//        echo form_input($data) . "<br>";


        echo "<div style=\"float:right;\">";
        echo form_submit('Submit', 'Send to Server');
        echo "</div>";
        echo form_fieldset_close();
        echo form_close();
        ?>
      </div>

      </br><hr></br>

      <div>
        Display the readings from server to the end users (View) ! 
        </br></br>

        <div id="display_screen_">

        </div>

      </div>
    </div>


    </br><hr>
  </body>
</html>