export default {
	data () {
		return {
			tableInit: false,
			emptyTable: false,
			page: {
				pageNum: 1,
				pageSize: 8,
				total: 0,
			},
		}
	},
	mounted () {
	},
	computed: {
		// emptyTable () {
		// 	return this.page.total === 0 && this.page.pageNum === 1 && this.emptyParam
		// },
	},
	watch: {
		'page.total' () {
			if (this.page.total > 0) {
				this.emptyTable = false
			}
		},
	},
	methods: {
		isObjectEmpty (data = {}) {
			return Object.values(data).filter(a => !!a).length === 0
		},
		setEmptyTable () {
			this.tableInit = true
			console.log(this.page.total, this.page.total == 0)
			this.emptyTable = this.page.total == 0
		},
		clearPage () {
			this.page.pageNum  = 1
		},
	},
}
