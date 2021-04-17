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
      methods: {
        getDataFromServer() {
          const brands = [
            {
              "id": 2032,
              "name": "OPPO",
              "image": "http://img10.360buyimg.com/popshop/jfs/t2119/133/2264148064/4303/b8ab3755/56b2f385N8e4eb051.jpg",
              "letter": "O",
              "categories": null
            },
            {
              "id": 2033,
              "name": "飞利浦（PHILIPS）",
              "image": "http://img12.360buyimg.com/popshop/jfs/t18361/122/1318410299/1870/36fe70c9/5ac43a4dNa44a0ce0.jpg",
              "letter": "F",
              "categories": null
            },
            {
              "id": 2034,
              "name": "华为（HUAWEI）",
              "image": "http://img10.360buyimg.com/popshop/jfs/t5662/36/8888655583/7806/1c629c01/598033b4Nd6055897.jpg",
              "letter": "H",
              "categories": null
            },
            {
              "id": 2036,
              "name": "酷派（Coolpad）",
              "image": "http://img10.360buyimg.com/popshop/jfs/t2521/347/883897149/3732/91c917ec/5670cf96Ncffa2ae6.jpg",
              "letter": "K",
              "categories": null
            },
            {
              "id": 2037,
              "name": "魅族（MEIZU）",
              "image": "http://img13.360buyimg.com/popshop/jfs/t3511/131/31887105/4943/48f83fa9/57fdf4b8N6e95624d.jpg",
              "letter": "M",
              "categories": null
            }
          ];

          setTimeout(()=>{
            this.brands = brands;
            this.totalBrands = brands.length;
            this.loading = false;
          }, 1000);
        }
      },
      mounted() {
        this.getDataFromServer();
      }
    
    }
    
</script>

<style scoped>

</style>