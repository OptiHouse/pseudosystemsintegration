<script lang="ts">
	import axios from 'axios';
	let transferringData = false;
	import { data } from '../../store';
	import { goto } from '$app/navigation';
	let message = '';

	let registerData = {
		username: '',
		password: '',
		confirm_password: ''
	};

	async function tryToRegister() {
		if (registerData.username.length > 0 && registerData.password.length > 0) {
			transferringData = true;
			let dataToSend = {
				username: registerData.username,
				password: registerData.password
			};
			try {
				let response = await axios.post(
					'http://localhost:8080/auth/register',
					JSON.stringify(dataToSend),
					{
						headers: {
							'Content-Type': 'application/json'
						}
					}
				);
				console.log(response);
				message = 'Registered!';
				goto('/login');
			} catch (error) {
				// @ts-ignore
				console.log(error);
				// @ts-ignore
				console.log(error.message);
				// @ts-ignore
				// if (error.message == 'Request failed with status code 401') {
				// message = 'Wrong username or password!';
				// } else {
				// @ts-ignore
				message = 'error: ' + error.message + '';
				// }
			}
			transferringData = false;
			localStorage.setItem('token', $data.token);
		} else {
			message = 'Please provide username and password!';
		}
	}
</script>

<div class="container h-full mx-auto flex justify-start items-center flex-col p-16">
	<div style="margin-left: 10px; display: flex; flex-direction: column; width: 300px; gap: 4px;">
		<div style="display: flex; justify-content: center;">
			<div style="font-size: 1.6rem;">REGISTER</div>
		</div>
		<p>username:</p>
		<input disabled={transferringData} type="text" bind:value={registerData.username} />
		<p>password</p>
		<input disabled={transferringData} type="password" bind:value={registerData.password} />
		<p>confirm password</p>
		<input disabled={transferringData} type="password" bind:value={registerData.confirm_password} />
		<button
			class="btn variant-filled"
			disabled={transferringData ||
				registerData.password != registerData.confirm_password ||
				!registerData.password ||
				!(registerData.username.length >= 3)}
			on:click={tryToRegister}
			>{registerData.username.length >= 3
				? registerData.password == registerData.confirm_password
					? 'Register'
					: 'passwords are not the same!'
				: 'username too short!'}</button
		>
	</div>
	{message}
</div>

<style>
	input {
		color: black;
	}
</style>
