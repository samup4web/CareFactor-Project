<html>
  <head>
    <title>Stub view</title>
    <link rel="stylesheet" type="text/css" href="<?php echo base_url() . "css/style.css"; ?>" />
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.js"></script>
    <script type="text/javascript"> 
      $(document).ready(function(){ 
        
        var data_format = "json";
        
        $("#json").click(function() {         
          data_format = "json";
          console.log(data_format);
        });
        
        $("#xml").click(function() {         
          data_format = "xml";
          console.log(data_format);
        });
        
        $("#none").click(function() {         
          data_format = "none";
          console.log(data_format);
        });
        
        $("#gateway_dummy_form").submit(function(event) {
          event.preventDefault(); 
           
          param_value = {
            "sensor_id" : $('#sensor_id').val(),
            "gateway_id" : $('#gateway_id').val(),
            "pos_longitude" : $('#pos_longitude').val(),
            "pos_latitude" : $('#pos_latitude').val(),
            "data_value" : $('#data_value').val(),
            "timestamp" : $('#timestamp').val()
          }  
          
          
          
          param_value_xml = "<xml>"+
            "<sensor_id>"+$('#sensor_id').val()+"</sensor_id>"+
            "<gateway_id>"+$('#gateway_id').val()+"</gateway_id>"+
            "<pos_longitude>"+$('#pos_longitude').val()+"</pos_longitude>"+
            "<pos_latitude>"+$('#pos_latitude').val()+"</pos_latitude>"+
            "<data_value>"+$('#data_value').val()+"</data_value>"+
            "<timestamp>"+$('#timestamp').val()+"</timestamp>"+
            "</xml>";
          
          console.log(param_value_xml);
          
          //          param_xml_urlencoded = '<?php echo urlencode("<xml> 
<sensor_id>sens240</sensor_id>
<gateway_id>gtwX31</gateway_id>
<pos_longitude>120.3244556</pos_longitude>
<pos_latitude>15.4356655</pos_latitude>
<data_value>24</data_value>
<timestamp>2011-04-12 12:12:12</timestamp>
</xml>"); ?>//;
      
      //param_xml_urlencoded = "%3Cxml%3E%20%0A%0A%20%20%20%20%3Csensor_id%3Esens240%3C%2Fsensor_id%3E%0A%0A%20%20%20%20%3Cgateway_id%3EgtwX31%3C%2Fgateway_id%3E%0A%0A%20%20%20%20%3Cpos_longitude%3E120.3244556%3C%2Fpos_longitude%3E%0A%0A%20%20%20%20%3Cpos_latitude%3E15.4356655%3C%2Fpos_latitude%3E%0A%0A%20%20%20%20%3Cdata_value%3E24%3C%2Fdata_value%3E%0A%0A%20%20%20%20%3Ctimestamp%3E2011-04-12%2012%3A12%3A12%3C%2Ftimestamp%3E%0A%0A%3C%2Fxml%3E%0A";
         
      param_xml_urlencoded = encodeURI(param_value_xml);
         
      param_value = array2json(param_value);
          
          
      url = '<?php echo base_url() . "api/submit/sensor_measurement/"; ?>';
          
      switch(data_format){
        case "json":
          param_body="param="+param_value;          
          url = '<?php echo base_url() . "api/submit/sensor_measurement/"; ?>';
          break;
        case "xml":
          param_body="param="+param_xml_urlencoded;              
          console.log(param_body);
          url = '<?php echo base_url() . "api/submit/sensor_measurement/format/xml"; ?>';
          break;
        case "none":
          param_body="";
          break;
        
        }
          
    
      
    
        $.ajax({
          type: "post",
          url: url ,
          contentType:  'application/x-www-form-urlencoded',
          data: param_body,
          dataType: "xml",
          error: function(request, error){
            var content = $( request.responseText ).find( 'message' );
              
            $( "#display_screen" ).empty().append("<span style=\"color:red\">input data invalid</span>");
            $( "#display_screen" ).append( "<br><br>"+request.responseText );       
            $("#display_screen").hide();
            $("#display_screen").show('fade');
          },
          success: function(request){
            var content = $( request ).find( 'message' );
               
            $( "#display_screen" ).empty().append( content );
            $("#display_screen").hide();
            $("#display_screen").show('fade');
          }
        });
 
 
          
      }); 
        
      var xml_special_to_escaped_one_map = {
        '&': '&amp;',
        '"': '&quot;',
        '<': '&lt;',
        '>': '&gt;'
      };
 
      var escaped_one_to_xml_special_map = {
        '&amp;': '&',
        '&quot;': '"',
        '&lt;': '<',
        '&gt;': '>'
      };
 
      function encodeXml(string) {
        return string.replace(/([\&"<>])/g, function(str, item) {
          return xml_special_to_escaped_one_map[item];
        });
      };
 
      function decodeXml(string) {
        return string.replace(/(&quot;|&lt;|&gt;|&amp;)/g,
        function(str, item) {
          return escaped_one_to_xml_special_map[item];
        });
      }
        
      function array2json(arr) {
        var parts = [];
        var is_list = (Object.prototype.toString.apply(arr) === '[object Array]');

        for(var key in arr) {
          var value = arr[key];
          if(typeof value == "object") { //Custom handling for arrays
            if(is_list) parts.push(array2json(value)); /* :RECURSION: */
            else parts[key] = array2json(value); /* :RECURSION: */
          } else {
            var str = "";
            if(!is_list) str = '"' + key + '":';

            //Custom handling for multiple data types
            if(typeof value == "number") str += value; //Numbers
            else if(value === false) str += 'false'; //The booleans
            else if(value === true) str += 'true';
            else str += '"' + value + '"'; //All other things
            // :TODO: Is there any more datatype we should be in the lookout for? (Functions?)

            parts.push(str);
          }
        }
        var json = parts.join(",");
    
        if(is_list) return '[' + json + ']';//Return numerical JSON
        return '{' + json + '}';//Return associative JSON
      }                
    });                  
    </script>
  </head>

  <body>
    <h1>Gateway Simulator</h1>
    <hr></br>
    <div id ='main'>
      <div>
        Gateway option to simulate the actual gateway </br></br>
        <?php
        $attributes = array('id' => 'gateway_dummy_form');
        echo form_open('/', $attributes);
        echo form_fieldset('Dummy Gateway');
        echo form_input('sensor_id', 'sensor_id', 'id="sensor_id"') . "<br>";
        echo form_input('gateway_id', 'gateway_id', 'id="gateway_id"') . "<br>";
        echo form_input('pos_longitude', 'long', 'id="pos_longitude"') . "<br>";
        echo form_input('pos_latitude', 'lat', 'id="pos_latitude"') . "<br>";
        echo form_input('data_value', 'value', 'id="data_value"') . "<br>";
        echo form_input('timestamp', 'timestamp', 'id="timestamp"');
        ?>

        <span style="padding-top: 9px;">

          <form>

            <input id="json" type="radio" name="rat" value="show_wcdma" checked/><span style="color: #E71B2D;  padding-right: 10px;"> JSON </span>
            <input id="xml" type="radio" name="rat" value="show_gsm" /><span style="color: #02306B; padding-right: 10px;"> XML </span>
            <input id="none" type="radio" name="rat" value="show_all" /> <span style="padding-right: 10px;"> NONE </span>

          </form>

        </span>

        <?php
        echo form_submit('button', 'Send to Server');

        echo form_fieldset_close();
        echo form_close();
        ?>
      </div>

      <div>
        Display response from server... (View) ! 
        </br></br>

        <div id="display_screen">

        </div>

      </div> 
    </div>



    </br><hr>
  </body>
</html>