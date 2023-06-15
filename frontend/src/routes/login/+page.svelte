<script lang="ts">
	import axios from 'axios';
	let transferringData = false;
	import { data } from '../../store';
	import { goto } from '$app/navigation';
	let message = '';

	let loginData = {
		username: '',
		password: ''
	};

	async function tryToLogin() {
		if (loginData.username.length > 0 && loginData.password.length > 0) {
			transferringData = true;
			let dataToSend = {
				username: loginData.username,
				password: loginData.password
			};
			try {
				let response = await axios.post(
					'http://localhost:8080/auth/login',
					JSON.stringify(dataToSend),
					{
						headers: {
							'Content-Type': 'application/json'
						}
					}
				);
				console.log(response.data.token);
				$data.token = response.data.token;
				message = 'Logged in!';
				goto('/');
			} catch (error) {
				// @ts-ignore
				console.log(error);
				// @ts-ignore
				console.log(error.message);
				// @ts-ignore
				if (error.message == 'Request failed with status code 401') {
					message = 'Wrong username or password!';
				} else {
					// @ts-ignore
					message = 'error: ' + error.message + '';
				}
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
			<div style="font-size: 1.6rem;">LOG IN</div>
		</div>
		<p>username:</p>
		<input disabled={transferringData} type="text" bind:value={loginData.username} />
		<p>password</p>
		<input disabled={transferringData} type="password" bind:value={loginData.password} />
		<button class="btn variant-filled" disabled={transferringData} on:click={tryToLogin}
			>Log in</button
		>
	</div>
	{message}
</div>

<style>
	input {
		color: black;
	}
</style>
