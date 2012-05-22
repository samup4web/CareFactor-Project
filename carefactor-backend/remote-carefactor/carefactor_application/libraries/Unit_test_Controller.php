<?php defined('BASEPATH') OR exit('No direct script access allowed');

class Unit_test_Controller extends CI_Controller {

    // Constructor function
    public function __construct()
    {
        parent::__construct();
        echo 'Unit_test_controller construct';
        $this->load->library('unit_tester');
        echo 'Unit_test controller contruct over !';
    }
    
    public function enable_strict_mode($bool){
        $this->unit->use_strict($bool);
    }
    public function build_report(){
        return $this->unit->report();
    }
    public function build_pretty_report(){
        $this->load->view('tests_report_layout',$this->unit->result());
    }
    
    public function perform_post_tests($function, $parameters, $expected_answer, $test_name = '', $formats = array('full_json', 'full_xml')) {
        $this->unit_tester->perform_post_tests($function, $parameters, $expected_answer, $test_name, $formats);
    }
    public function perform_get_tests($function, $parameters, $expected_answer, $test_name = '', $formats = array('full_json', 'full_xml')) {
        $this->unit_tester->perform_get_tests($function, $parameters, $expected_answer, $test_name = '', $formats = array('full_json', 'full_xml'));
    }
    
}