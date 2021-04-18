<template>
  <v-card>
    
    <v-card-title flat color="white">
      <v-btn color="primary">新增</v-btn>
      <v-spacer></v-spacer>
      <v-text-field lable="输入关键字搜索" v-model="search" append-icon="search" hide-details></v-text-field>
    </v-card-title>

    <v-data-table
      :headers="headers"
      :items="brands"
      :pagination.sync="pagination"
      :total-items="totalBrands"
      :loading="loading"
      class="elevation-1"
    >

      <template slot="items" slot-scope="props">
        <td>{{props.item.name}}</td>
        <td class="text-xs-center">{{props.item.id}}</td>
        <td class="text-xs-center">{{props.item.name}}</td>
        <td class="text-xs-center">
          <img v-if="props.item.image"  :src="props.item.image" width="130" height="40">
        </td>
        <td class="text-xs-center">{{props.item.letter}}</td>
        <td class="text-xs-center">
          <v-icon small class="mr-2" @click="editItem(props.item)">
              edit
          </v-icon>
          <v-icon small @click="deleteItem(props.item)">
              delete
          </v-icon>         
        </td>
      </template>
    
    </v-data-table>

  </v-card>
</template>

<script>
    export default {
      name: "MyBrand",
      data() {
        return {
          totalBrands: 0,
          brands: [],
          search:"",
          loading: true,
          pagination :{},
          headers: [
            {text: 'id', align: 'center', value: 'id'},
            {text: '名称', align: 'center', value: 'name', sortable: false},
            {text:'LOGO', align: 'center', value: 'image', sortable: false},
            {text:'首字母', align:'center', value:'letter'},
            {text: '操作', align: 'center', value: 'id', sortable: false }
          ]
        }
      },
      watch: {
        pagination : {
          deep: true,
          handler() {
            this.getDataFromServer();
          }
        },
        search: {
          handler(){
            this.getDataFromServer();
          }
        }
      },
      methods: {
        getDataFromServer() {
          this.loading = true;
          this.$http.get("/item/brand/page", {
            params : {
              page: this.pagination.page,
              rows: this.pagination.rowsPerPage,
              sortBy: this.pagination.sortBy,
              desc: this.pagination.descending,
              key: this.search
            }
          } )
          .then(
            resp => {
              this.totalBrands = resp.data.total;
              this.brands = resp.data.items;
              this.loading = false;
            }

          );
        }
      },
      mounted() {
        this.getDataFromServer();
      }
    
    }
    
</script>

<style scoped>

</style>