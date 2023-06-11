<script lang="ts">
	let loading = false;
	let justLoaded = false;
	export let awaitedFunction: () => Promise<any>;
	export let myData: any;

	async function loadData() {
		loading = true;
		let response = await awaitedFunction();
		loading = false;
		justLoaded = true;
		setTimeout(() => {
			justLoaded = false;
		}, 3000);
		console.log(response);
		myData = response.data;
		return {
			response
		};
	}

	import { ProgressRadial } from '@skeletonlabs/skeleton';
</script>

<button class="btn variant-filled" disabled={loading} on:click={loadData}>
	{#if !loading}
		load data
	{:else}
		loading...
	{/if}
</button>
<!-- <div style="position: relative; right: 200px;"> -->
{#if loading || justLoaded}
	<ProgressRadial
		value={justLoaded ? 100 : undefined}
		stroke={10}
		track={justLoaded ? 'stroke-success-500/30' : 'stroke-primary-500/30'}
		meter={justLoaded ? 'stroke-success-500' : 'stroke-primary-500'}
	/>
{/if}
