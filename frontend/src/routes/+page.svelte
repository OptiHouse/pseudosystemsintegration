<script lang="ts">
	import axios from 'axios';
	import RequestButton from '$lib/requestButton.svelte';
	let dataLoadingResponse = '';
	let currently_analyzed_data: any = [];
	let calculating = false;
	let sorted_by = '?';

	function reorder_by_rate() {
		calculating = true;
		console.log(currently_analyzed_data);
		if (sorted_by != 'rate_asc') {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.statistics[0]?.crimes[0]?.rate - 0 > b?.statistics[0]?.crimes[0]?.rate - 0
					? 1
					: -1;
			});
			sorted_by = 'rate_asc';
		} else {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.statistics[0]?.crimes[0]?.rate - 0 < b?.statistics[0]?.crimes[0]?.rate - 0
					? 1
					: -1;
			});
			sorted_by = 'rate_desc';
		}
		calculating = false;
	}

	function reorder_by_state() {
		calculating = true;
		console.log(currently_analyzed_data);
		if (sorted_by != 'state_asc') {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.name > b?.name ? 1 : -1;
			});
			sorted_by = 'state_asc';
		} else {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.name < b?.name ? 1 : -1;
			});
			sorted_by = 'state_desc';
		}
		calculating = false;
	}

	function reorder_by_race(race) {
		calculating = true;
		console.log(currently_analyzed_data);
		if (sorted_by != `${race}_asc`) {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				// currently_analyzed_data[0]?.statistics[0]?.population[i]?.name
				return a?.statistics?.[0]?.population[race]?.population - 0 >
					b?.statistics?.[0]?.population[race]?.population - 0
					? 1
					: -1;
				// return a?.name > b?.name ? 1 : -1;
			});
			sorted_by = `${race}_asc`;
		} else {
			currently_analyzed_data = currently_analyzed_data.sort((a, b) => {
				return a?.statistics?.[0]?.population[race]?.population - 0 <
					b?.statistics?.[0]?.population[race]?.population - 0
					? 1
					: -1;
			});
			sorted_by = `${race}_desc`;
		}
		calculating = false;
	}
</script>

<div class="container h-full mx-auto flex justify-start items-center flex-col p-16">
	<RequestButton
		text="load data to DB"
		to_be_sorted={false}
		bind:responseData={dataLoadingResponse}
		awaitedFunction={() => {
			return axios.post('http://localhost:8080/data/loadData', {
				headers: {
					'Content-Type': 'application/json',
					'Access-Control-Allow-Origin': '*'
				}
			});
		}}
	/>
	<RequestButton
		text="get data from DB"
		bind:responseData={currently_analyzed_data}
		awaitedFunction={() => {
			return axios.get('http://localhost:8080/graphs?years=2013&crimes=Murder', {
				headers: {
					'Content-Type': 'application/json',
					'Access-Control-Allow-Origin': '*'
				}
			});
		}}
	/>

	{#if currently_analyzed_data.length}
		<div class="table-container" style="margin-top: 8px;">
			<table class="table table-hover">
				<thead>
					<tr>
						<th><button disabled={calculating} on:click={reorder_by_state}>Name</button></th>
						<th><button disabled={calculating} on:click={reorder_by_rate}>Rate</button></th>
						{#if currently_analyzed_data.length > 1}
							{#each { length: currently_analyzed_data[0]?.statistics[0]?.population?.length } as _, i}
								<th>
									<button
										disabled={calculating}
										on:click={() => {
											console.log(i);
											reorder_by_race(i);
										}}>{currently_analyzed_data[0]?.statistics[0]?.population[i]?.name}</button
									>
								</th>
							{/each}
						{/if}
					</tr>
				</thead>
				<tbody>
					{#each currently_analyzed_data as row, i}
						{#if row.name != 'State' && row.name != 'United States'}
							<tr>
								<td>{row.name}</td>
								<td>{row.statistics[0].crimes[0].rate}</td>
								{#each { length: currently_analyzed_data[0]?.statistics[0]?.population?.length } as _, i}
									<td>{Math.round(row?.statistics[0]?.population[i]?.population * 100) / 100}</td>
								{/each}
							</tr>
						{/if}
					{/each}
				</tbody>
			</table>
		</div>
	{/if}

	<div>
		{#if dataLoadingResponse}
			dataLoadingResponse
		{/if}
	</div>
</div>
