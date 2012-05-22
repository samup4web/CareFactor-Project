<?php
/**
 * Format class
 *
 * Help convert between various formats such as XML, JSON, CSV, etc.
 *
 * @author		Phil Sturgeon
 * @license		http://philsturgeon.co.uk/code/dbad-license
 */
class Decoder {
    
    function decode_input($input, $format){
        $decoder = $this->pick_decoder($format);
        return $decoder->decode_data($input);
    }
    
    function decode_input_array($input_array, $format){
        $decoded_array = array();
        $decoder = $this->pick_decoder($format);
        foreach($input_array as $input){
         $decoded_array[] = $decoder->decode_data($input);
        }
        return $decoded_array;
    }
    
    function pick_decoder($format){
        $format = trim($format);
//        echo 'I am gonna pick a decoder suitable for the following format: ';
//        print_r($format);
//        echo '<br/>';
        $CI =& get_instance();
//        echo 'I got the instance !';
        $decoder = false;
        if($format == false){
//            echo 'I need to use the config to choose the format';
            $CI->config->load('rest');
            $format = $CI->config->item('rest_default_format');
        }
//        echo 'I am gonna switch on format -> '.var_dump($format).' against the following format: '. var_dump('json').' and '.  var_dump('xml');
        switch($format){
        case 'json':
//            echo 'I will pick Json_decode !';
            $decoder = new Json_decode();
            break;
        case 'xml':
//            echo 'I will pick Xml_decode !';
            $decoder = new Xml_decode();
            break;
        default:
//            echo "\r\n".'<br/> unsupported format : '.$format;
            break;
        }
        return $decoder;
    }
}

abstract class Decode_method{
    abstract function decode_data($data);
}
class Json_decode extends Decode_method{
    function decode_data($data) {
        return json_decode($data, true);
    }
}
class Xml_decode extends Decode_method{
    function decode_data($data) {
//        $data = to_utf8($data);
        $data = html_entity_decode($data);
//        $data = base64_decode($data);
//        echo 'I am gonna decode this ---> ';
//        var_dump($data);
        return (array) simplexml_load_string($data);
    }
}