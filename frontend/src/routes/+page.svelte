<script lang="ts">
	import axios from 'axios';
	import RequestButton from '$lib/requestButton.svelte';
	let myData = '';
</script>

<div class="container h-full mx-auto flex justify-center items-center">
	<RequestButton
		bind:myData
		awaitedFunction={() => {
			return axios.post('http://localhost:8080/data/loadData', {
				// return axios.get('http://localhost:8069/posts', {
				headers: {
					'Content-Type': 'application/json'
				}
			});
		}}
	/>

	<div>
		{#if myData}
			myData exists :D
			<!-- {#each myData as post}
				<div class="card">
					<div class="card-body">
						<h5 class="card-title"><b>{post.title}</b></h5>
						<p class="card-text">{post.text}</p>
					</div>
				</div>
			{/each} -->
		{/if}
	</div>
</div>
