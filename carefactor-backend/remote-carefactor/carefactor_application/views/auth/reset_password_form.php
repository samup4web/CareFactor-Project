
<div style="font-size: 14px; font-family: sans-serif;  padding: 5px 5px 5px 5px;  color: #346a2a; ">
    
    <img src="<?php echo base_url() . "images/home_logo.jpg"; ?>" /><br><br>

Welcome to CareFactor! <?php echo anchor('https://www.facebook.com/pages/CareFactor/281976968505841', 'Visit our Facebook page'); ?> <br> <br>

Link to Our page on Ericsson Application Awards Website! <?php echo anchor('http://www.ericssonapplicationawards.com/team/profile/carefactor', 'here'); ?> <br> <br>
<span>
A solution to help ease challenges with food waste and logistics. Our application aims to decrease food waste by optimizing the distribution channels which is an essential part of supply chain management. 

The Team CareFactor takes pleasure in using technology to support a solution to an important sustainability problem. </span>

<br> <br>  

</div>

<?php
$new_password = array(
	'name'	=> 'new_password',
	'id'	=> 'new_password',
	'maxlength'	=> $this->config->item('password_max_length', 'tank_auth'),
	'size'	=> 30,
);
$confirm_new_password = array(
	'name'	=> 'confirm_new_password',
	'id'	=> 'confirm_new_password',
	'maxlength'	=> $this->config->item('password_max_length', 'tank_auth'),
	'size' 	=> 30,
);
?>
<?php echo form_open($this->uri->uri_string()); ?>
<table>
	<tr>
		<td><?php echo form_label('New Password', $new_password['id']); ?></td>
		<td><?php echo form_password($new_password); ?></td>
		<td style="color: red;"><?php echo form_error($new_password['name']); ?><?php echo isset($errors[$new_password['name']])?$errors[$new_password['name']]:''; ?></td>
	</tr>
	<tr>
		<td><?php echo form_label('Confirm New Password', $confirm_new_password['id']); ?></td>
		<td><?php echo form_password($confirm_new_password); ?></td>
		<td style="color: red;"><?php echo form_error($confirm_new_password['name']); ?><?php echo isset($errors[$confirm_new_password['name']])?$errors[$confirm_new_password['name']]:''; ?></td>
	</tr>
</table>
<?php echo form_submit('change', 'Change Password'); ?>
<?php echo form_close(); ?>