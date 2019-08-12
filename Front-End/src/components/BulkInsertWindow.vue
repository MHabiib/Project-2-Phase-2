<template>
  <div class='fixedPosition'>
    <div class='createBulkInsertWindow'>
      <div class='createBulkInsertWindowSize'>
        <div class="createBulkInsertHeader">
          <div>Bulk Insert</div>
          <div class='buttonGroup'>
            <!-- <div class="closeButton" @click="closeBulkInsertWindow">Close</div> -->
            <div class="editButton" @click="closeBulkInsertWindow">Close</div>
            <!-- <div  class="editButton"  @click="closeBulkInsertWindow">{{closeBtnName}}</div>           -->
          </div>
        </div>
        <div class="createBulkInsertBody">
          <div >
            <div @click="downloadFile" class="downloadButton">
                Download Template
            </div>
            <div class="singleLineInput" >
              Please upload excel file below
            </div>
            <div class="fileInput" >
            <input 
                accept=".xlsx, .xls, .csv" 
                type="file" 
                @change="changeFile($event)" 
                class="selectFile"
                placeholder="Excel file"/>
            </div>
            <div  @click="uploadFile" class="uploadButton">
                Upload
            </div>
            <div v-show="loading" style="text-align:center">
                <div class="lds-ring"><div></div><div></div><div></div><div></div></div>
            </div>
          </div>       
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Helper from '../../Helper';
  import Download from '../../download';

  import Members from "../page/Members";

  export default {
    components:{
    },
    data: function() {
      return {
        newBulkInsert: {},
        groupMemberList:[],
        disableButton: false,
        loading:false,
        fileInput:null,
        reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/
      }
    },
    computed:{
      editBtnName: function(){
        return this.editButtonNames[this.edit].name
      },
      closeBtnName: function(){
        return this.closeButtonNames[this.edit].name
      }
    },
    created() {

    },
    methods: {                 
      downloadFile(){ //get file template excel
        fetch(`${Helper.backEndAddress}/api/file`,{
        // fetch(`http://localhost:8081/file`,{
          headers: {
            Authorization: localStorage.getItem('accessToken'),
            },
          method:'GET',
        }).then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.downloadFile)
            } else {
                localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
                if(response.ok){
                  response.blob()
                  .then(blob =>{
                    Download(blob,"template.xlsx","application/vnd.ms-excel")
                  })
                }
            }
        })
      },
      uploadFile(){
        if(this.fileInput==null){ alert('Please upload excel file.'); return}
        this.createBulkInsert()
      },
      createBulkInsert(){
        // if(this.loading=true) return
        let formData = new FormData();
        formData.append('file',this.fileInput)
        this.loading=true
        fetch(`${Helper.backEndAddress}/api/file`,{
          headers: {
            Authorization: localStorage.getItem('accessToken'),
            // 'Content-Type': 'multipart/form-data'
        },
          method:'POST',
          body:formData
        }).then(response => {
            if(response.status==401){
              console.log('groupId : '+this.groupId)
              Helper.getNewToken(this.createBulkInsert)
            } else {
                localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
                if(response.ok){
                  setTimeout(e=>{this.loading=false},500)                     
                  alert('bulk insert succeed!');
                  this.$emit('updateBulkInsertWindow');
                }
            }
        })
      },
      changeFile(e) {
         if(e.target.files[0].size > 5000000) {
          alert('Max File size is 5 MB. Please upload a lower resolution image.');
          this.fileInput = null;
          e.target.value = null;
        } else {
        this.fileInput = e.target.files[0];
        console.log(this.fileInput)
        }
      },
      closeBulkInsertWindow() {
        this.$emit('closeBulkInsertWindow'); return
      },
    },
  }
</script>

<style>
  .fixedPosition {
    position: absolute;
  }
  .red {
    color: RED;
  }
  .createBulkInsertWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .createBulkInsertHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px 20px;
    box-sizing: border-box;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    width: 95%;
    margin: auto;
    position: relative;
    z-index: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
  }

  .createBulkInsertWindowSize {
    width: 40%;
    height: fit-content;
  }

  .createBulkInsertBody {
    background-color: white;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    top: -30px;
    padding: 20px;
    padding-top: 32px;
    height: 100%;
  }

  .closeButton {
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
  }
  .editButton{
    font-size: 12px;
    padding: 7px; 
    /* color: var(--primary-0); */
    border-radius: 50%;
    cursor: pointer;
    font-weight: 400;
    margin-left: 10px;
  }
  .editButton:hover{
    color: pointer;
    background-color: #fff;  
    color: var(--primary-0);
  }
.uploadButton, .downloadButton{
    margin-top: 15px;
    text-align: center;
    font-size: 14px;
    padding: 7px; 
    color: #fff;
    background-color: var(--primary-0);
    /* color: var(--primary-0); */
    border-radius: 5px;
    cursor: pointer;
    font-weight: 400;
    margin-left: 10px;
    }

    .uploadButton:hover , .downloadButton:hover{
        color: pointer;
        background-color: var(--lightColor);  
        color: var(--primary-0);
    }

  .okButton {
    background-color: #fff;
    padding: 7px;
    color: var(--primary-0);
    border-radius: 50%;
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
    margin-left: 10px;
  }

  .buttonGroup {
    display: flex;
    align-items: center;
  }

  .singleLineInput {
    outline: none;
    padding: 5px;
    width: 100%;
    /* border: solid 1px var(--primary-1); */
    /* border-top: none;
    border-left: none;
    border-right: none; */
    color: var(--primary-3);
    margin-top: 15px;
    /* box-sizing: border-box; */
    border-radius: 2px;
  }
  
  .singleLineInput::placeholder {
    color: var(--primary-1);
  }
  .fileInput{
      text-align: center;
  }
  .selectFile {
    outline: none;
    border: none;
    padding: 10px !important;
    color: var(--primary-4);
    width: 80%;
    /* height: 40px; */
    background-color: var(--lightColor);
    margin-top: 15px;
    margin-right: 12px;
    border-radius: 10px;
  }

  .selectFile:hover{
    cursor: pointer;
    color:white;
    background-color: var(--primary-1);
  }
  .selectFileOption{
    padding: 10px;
    color: var(--primary-3);
  }
  .lds-ring {
    display: block;
    position: absolute;
    top:43%;
    right:43%;
    width: 64px;
    height: 64px;
  }
  .lds-ring div {
    box-sizing: border-box;
    display: flex;
    position: absolute;
    width: 51px;
    height: 51px;
    margin: 6px;
    border: 6px solid var(--primary-2);
    border-radius: 50%;
    animation: lds-ring 1.2s cubic-bezier(0.5, 0, 0.5, 1) infinite;
    border-color: var(--primary-2) transparent transparent transparent;
  }
  .lds-ring div:nth-child(1) {
    animation-delay: -0.45s;
  }
  .lds-ring div:nth-child(2) {
    animation-delay: -0.3s;
  }
  .lds-ring div:nth-child(3) {
    animation-delay: -0.15s;
  }
  @keyframes lds-ring {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }

</style>
