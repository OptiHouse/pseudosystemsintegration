<script lang="ts">
	let loading = false;
	let justLoaded = false;
	export let awaitedFunction: () => Promise<any>;
	export let responseData: any;
	export let text: string;
	export let to_be_sorted = true;

	async function loadData() {
		loading = true;
		let response = await awaitedFunction();
		loading = false;
		justLoaded = true;
		setTimeout(() => {
			justLoaded = false;
		}, 3000);
		console.log(response);
		if (to_be_sorted) {
			for (let i = 0; i < response.data.length; i++) {
				// console.log(response.data[i]?.statistics[0]);
				response.data = response.data.filter(
					(elem) => elem.name != 'United States' && elem.name != 'State'
				);
				response.data[i].statistics[0]?.population.sort((a, b) => {
					if (a.name > b.name) {
						return 1;
					} else {
						return -1;
					}
				});
			}
		}
		responseData = response.data;
		return {
			response
		};
	}

	import { ProgressRadial } from '@skeletonlabs/skeleton';
</script>

<link
	rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"
/>

<div style="display: flex; flex-direction: row;">
	<button
		class="btn variant-filled"
		style="width: 200px; margin: 4px;"
		disabled={loading}
		on:click={loadData}
	>
		{#if !loading}
			<i style="margin-right: 4px;" class="fa fa-database" /> {text}
		{:else}
			<!-- loading... -->
			<i style="margin-right: 4px;" class="fa fa-spinner fa-spin" />Loading
		{/if}
	</button>
	<!-- <div style="position: relative; right: 200px;"> -->
	<!-- <div style="position:relative; right: 200px;">
		{#if loading || justLoaded}
			<ProgressRadial
				value={justLoaded ? 100 : undefined}
				stroke={10}
				track={justLoaded ? 'stroke-success-500/30' : 'stroke-primary-500/30'}
				meter={justLoaded ? 'stroke-success-500' : 'stroke-primary-500'}
			/>
		{/if}
	</div> -->
</div>
