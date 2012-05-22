<?php

/**
 * Format class
 *
 * Help to run unit tests on API request
 *
 * @author		leogs
 */
class Unit_tester {

    public $json = 'full_json';
    public $xml = 'full_xml';
    public $CI;
    
    public function __construct() {
        echo 'Unit_tester lib construct ';
        $CI =& get_instance();
        $CI->load->library('curl');
        $CI->load->library('unit_test');
        echo 'Unit_tester lib construct over !';
    }

    //a method to create json and xml entry for an array one
    public function _from_table_to_json($array) {
        return json_encode($array);
    }

    public function _from_table_to_xml($array) {
        return $this->toXml($array);
    }

    //TODO : decode the answer instead of encoding the expected answer ! ! ! ! 
    //a post testing method
    public function perform_post_tests($function, $parameters, $expected_answer, $test_name, $formats) {
//        $CI =& get_instance();
        if (!empty($test_name)) {
            $test_name .= ' - ';
        }
        if (in_array($this->json, $formats)) {
            $json_params = $this->_create_json_parameters($parameters);
            $actual_answer = $CI->curl->simple_post($function, $json_params);
            $CI->unit->run($actual_answer, $this->_from_table_to_json($expected_answer), $test_name . '(full JSON)');
        }
        if (in_array($this->xml, $formats)) {
            $xml_params = $this->_create_xml_parameters($parameters);
            if(substr($function, -1)!= '/'){
                $function .= '/';
            }
            $actual_answer = $CI->curl->simple_post($function.'format/xml', $xml_params);
            $CI->unit->run($actual_answer, $this->_from_table_to_xml($expected_answer), $test_name . '(full JSON)');
        }
    }

    //a get testing method
    public function perform_get_tests($function, $parameters, $expeted_answer, $test_name, $formats) {
//        $CI =& get_instance();

        if (!empty($test_name)) {
            $test_name .= ' - ';
        }
        if (in_array($this->json, $formats)) {
            $actual_answer = $CI->curl->simple_get($function, $this->_from_table_to_json($parameters));
            $CI->unit->run($actual_answer, $this->_from_table_to_json($expected_answer), $test_name . '(full JSON)');
        }
        if (in_array($this->xml, $formats)) {
            if(substr($function, -1)!= '/'){
                $function .= '/';
            }
            $actual_answer = $CI->curl->simple_get($function.'format/xml', $this->_from_table_to_xml($parameters));
            $CI->unit->run($actual_answer, $this->_from_table_to_xml($expected_answer), $test_name . '(full JSON)');
        }
    }

    //this function transform all the sub array into a json string
    function _create_json_parameters($parameters) {
        return $this->_create_parameters_from_encoder(new JSON_Encoder(), $parameters);
    }

    //this function transform all the sub array into a xml string
    function _create_xml_parameters($parameters) {
        return $this->_create_parameters_from_encoder(new XML_Encoder(), $parameters);
    }

    //this function transform all the sub array using the encode method of the given encoder
    function _create_parameters_from_encoder($encoder, $parameters) {
        if (is_array($parameters)) {

            $encoded_parameters = Array();
            foreach ($parameters as $param_id => $param_value) {
                if (is_array($param_value)) {
                    $encoded_parameters[$param_id] = $encoder->encode($param_value);
                } elseif (is_string($param_value)) {
                    $encoded_parameters[$param_id] = $param_value;
                }
            }
            return $encoded_parameters;
        } elseif (is_string($parameters)) {
            return $parameters;
        } else {
            throw new Exception('the given $parameter is neither a string nor an array... please check it\'s format');
        }
    }

}

abstract class Encoder {

    abstract function encode($data);
}

class JSON_Encoder extends Encoder {

    function encode($data) {
        return json_encode($data);
    }

}

class XML_Encoder extends Encoder {

    function encode($data) {
        return $this->toXml($data);
    }

    public function toXml($data, $rootNodeName = 'xml', $xml=null) {
// turn off compatibility mode as simple xml throws a wobbly if you don't.
        if (ini_get('zend.ze1_compatibility_mode') == 1) {
            ini_set('zend.ze1_compatibility_mode', 0);
        }

        if ($xml == null) {
            $xml = simplexml_load_string("<?xml version='1.0' encoding='utf-8'?><$rootNodeName />");
//            $xml = simplexml_load_string("<$rootNodeName />");
        }

// loop through the data passed in.
        foreach ($data as $key => $value) {
// no numeric keys in our xml please!
            if (is_numeric($key)) {
// make string key...
                $key = 'item';
            }

// replace anything not alpha numeric
//            $key = preg_replace('/[^a-z_\ ]/i', '', $key);

// if there is another array found recrusively call this function
            if (is_array($value)) {
                $node = $xml->addChild($key);
// recrusive call.
                ArrayToXML::toXml($value, $rootNodeName, $node);
            } else {
// add single node.
                $value = htmlentities($value);
                $xml->addChild($key, $value);
            }
        }
// pass back as string. or simple xml object if you want!
        return $xml->asXML();
    }

}

/* End of file format.php */