<script>
	import { Scatter } from 'svelte-chartjs';
	// import { data } from './chartdata';
	export let chartData;
	export let stretchGraphs;

	import {
		Chart as ChartJS,
		Title,
		Tooltip,
		Legend,
		LineElement,
		CategoryScale,
		LinearScale,
		PointElement
	} from 'chart.js';

	ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement);
</script>

<Scatter
	data={chartData}
	options={{
		responsive: true,
		scales: {
			x: {
				type: 'linear',
				position: 'bottom',
				title: {
					display: true,
					text: 'race %'
				},
				min: stretchGraphs ? undefined : 0,
				max: stretchGraphs ? undefined : 100
			},
			y: {
				type: 'linear',
				position: 'left',
				title: {
					display: true,
					text: 'crime rate'
				}
			}
		}
	}}
/>
