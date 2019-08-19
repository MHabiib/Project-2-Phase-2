<template>
  <div class='fixedPosition'>
    <div class='payNowWindow'>
      <div class='payNowWindowSize'>
        <div class="payNowHeader">
          <div>
            <div class='textPayNow'>Pay Now</div>
            <div class='pembayaranTerakhir'>Pembayaran terakhir Anda: <b>{{lastPayment}}</b></div>
          </div>

          <div class='buttonGroup'>
            <div class="closeButton" @click="closePayNowWindow">Close</div>
            <div class="okButton" @click='submitPembayaran'>OK</div>
          </div>
        </div>

        <div class="payNowBody">
          <div class="payNowOneRow">
            Transfer to account number: {{dataPayNow.nomorRekening}} a.n. {{dataPayNow.namaAdmin}}
          </div>

          <div class="payNowOneRow">
            <div style="width: 45%">
              <input style="width: 90%"  @keypress="checkChar" type="number" min="1" max="99" maxlength="2" name="periode" id="periode" placeholder="Number of periods" v-model='periode'/>
            </div>
            <div style="text-align:left">
              Total harus dibayar: Rp {{totalTagihan | thousandSeparators}}
            </div>
          </div>

          <div class="payNowOneRow">
              <input style="width: 40%"
              type="text"
              name="rekeningPengirim"
              id="rekeningPengirim"
              placeholder="Bank Account Number"
              @keypress='checkChar'
              v-model='nomorRekeningPengirim'
            />

            <input 
              style="flex: 1;width:40%"
              type="text"
              name="namaPengirim"
              id="namaPengirim"
              placeholder="Bank Account Name"
              v-model='namaPengirim'
            />
          </div>

          <div class="payNowOneRow">
              <input style="width: 40%"
              type="date"
              name="tanggalTransfer"
              id="tanggalTransfer"
              placeholder=""
              v-model='tanggalTransfer'
            />
            
            <input
              type="file"
              name="buktiTransfer"
              id="buktiTransfer"
              @change="selectFile($event)"
            />
          </div>

          <div class="payNowOneRow">
            <input @click="setUntukMemberLain" type="checkbox" name="untukMemberLain" id="untukMemberLain"/>
            <div style="margin-right: 10px;">Pay for other member?</div>
             <div class="dropdownMenu" style="flex: 1" >
                <multiselect 
                  v-if="untukMemberLain"
                  v-model="emailMemberLain" 
                  :allow-empty="false" 
                  :options="groupMemberList" 
                  :searchable="true" 
                  :close-on-select="true" 
                  :show-labels="false" 
                  placeholder="Pick a member">
                </multiselect>
            </div>
            <!-- <input
              style="flex: 1"
              type="text"
              name="emailMemberLain"
              id="emailMemberLain"
              placeholder="Other member's email account"
              v-if="untukMemberLain"
              v-model="emailMemberLain"
            /> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { backEndAddress } from '../../Helper';
import Helper from '../../Helper';
import Multiselect from 'vue-multiselect'

const monthList =["January","February","March","April","May","June","July","August","September","Oktober","November","Desember"]
  export default {
    props: ['dataPayNow'],
    components:{
      'multiselect': Multiselect,
    },
    data: function() {
      return {
        searchQuery: '',
        groupMemberList:[],
        biaya: '',
        periode: '',
        untukMemberLain: false,
        emailMemberLain: '',
        nomorRekeningPengirim: '',
        namaPengirim: '',
        tanggalTransfer: '',
        buktiTransfer: null,
        regularPayment:0,
        myEmail:localStorage.getItem('userEmail'),
        reg: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,24}))$/
      }
    },
    computed: {
      totalTagihan: function() {
        return (this.periode * this.dataPayNow.regularPayment)
      },
      lastPayment: function(){
        return (monthList[this.dataPayNow.lastPayment])
      }
    },
    created() {
      // console.log(this.dataPayNow);
      this.getMember();
    },
    methods: {
      closePayNowWindow() {
        this.$emit('closePayNowWindow')
      },
      checkChar(e) {
        if(e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode === 8) {} else {
        // if(e.value <0 && e.value > 9){
          e.preventDefault();
        }
      },
      setUntukMemberLain() {
        this.untukMemberLain = !this.untukMemberLain;
        if(this.untukMemberLain === false) {
          this.emailMemberLain = '';
        }
      },
      selectFile(e) {
        if(e.target.files[0].size > 5000000) {
          alert('Max Image size is 5 MB. Please upload a lower resolution image.');
          this.buktiTransfer = null;
          e.target.value = null;
        } else {
          this.buktiTransfer = e.target.files[0];
        }
      },
      validateInput(emailInput){
        if(this.periode ==="") {
          alert('Periode can\'t be null.')
          return false
        } if(this.untukMemberLain==true){
            if(this.emailMemberLain==='') {
                alert('Please input a valid email.')
                return false
            }
        } else if(this.nomorRekeningPengirim === '') {
          alert('Please input bank account\'s name')
          return false        
        } else if(this.namaPengirim === '') {
          alert('Please input bank account\'s name')
          return false
        }else if(this.buktiTransfer === null) {
          alert('Please input the proof of payment.')
          return false
        } else if(new Date(this.tanggalTransfer).getTime() <= 25200000){
          alert('Please input payment date.')
          return false
        }
        return true
      
        // return (this.email == "")? "" : (this.reg.test(this.email)) ? 'has-success' : 'has-error';
      },
      submitPembayaran() {
        if(!this.validateInput()) return;
        console.log('Tgl TF: '+ new Date(this.tanggalTransfer).getTime())
        let paymentDateInMillis = new Date(this.tanggalTransfer).getTime()
        let formData = new FormData();
        formData.append('payment', JSON.stringify({
          periode: this.periode,
          nomorRekeningPengirim: this.nomorRekeningPengirim,
          namaPengirim: this.namaPengirim,
          paymentDate: paymentDateInMillis,
          emailMemberLain: this.untukMemberLain ? this.emailMemberLain : "",
          email: localStorage.getItem('userEmail'),
          price: this.totalTagihan
        }))

        formData.append('file', this.buktiTransfer);

        fetch(`${backEndAddress}/api/payment`, {
          method: 'POST',
          headers: {
            Authorization: localStorage.getItem('accessToken')
          },
          body: formData
        })
        .then(response => {
          if(response.status==401){
            Helper.getNewToken(this.submitPembayaran)
          }
          else if(response.ok) {
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            alert('Succeed!\nWaiting for group admin confirmation.');
            this.closePayNowWindow();
          } else {
            // console.log(response);
            alert(`Oops! Something wrong happened ${response.status}.`)
          }
        })
        .catch(err => {
  
            alert(`Oops! Something wrong happened ${response.status}.`)// console.log(err);
        })
      },
    getMember() {
      fetch(`${Helper.backEndAddress}/api/group/members`, {
        headers: {
          Authorization: localStorage.getItem('accessToken')
        },
        method: 'GET'
      })
        .then(response => {
          if(response.status==401){
            this.groupMemberList=[]
            Helper.getNewToken(this.getMember)
          }
          else if(response.ok){
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            response.json().then(
              res => {
                // console.log(res)
                this.groupMemberList.length=0
                res.forEach( element =>{
                  this.groupMemberList.push(element.email)
                })
                let index = this.groupMemberList.indexOf(this.myEmail);
                if (index !== -1) this.groupMemberList.splice(index, 1);
                setTimeout(e=>{this.loading=false},500)
              }
            )
          }
          else{
            localStorage.setItem('accessToken','Token '+response.headers.get("Authorization"))
            alert('Oops! Something wrong happened when processing request.\nPlease try again.')
          }
        })
      }
    },
    
  }
</script>
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>
  .fixedPosition {
    position: absolute;
  }

  .payNowWindow {
    display: flex;
    width: 100vw;
    height: 100vh;
    align-items: center;
    justify-content: space-evenly;
    background-color: rgba(0, 0, 0, .2);
    z-index: 2;
    position: absolute;
  }

  .payNowHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 16px;
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
  }

  .payNowWindowSize {
    width: 35%;
    height: 30%;
    position: relative;
    top: -60px;
  }

  .payNowBody {
    background-color: white;
    border-radius: 5px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    top: -30px;
    padding: 20px;
    padding-top: 32px;
    height: 100%;
    color: var(--primary-4);
    font-size: 14px;
  }

  .closeButton {
    font-size: 12px;
    cursor: pointer;
    font-weight: 400;
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

  .textPayNow {
    font-weight: bold;
    font-size: 18px;
  }

  .pembayaranTerakhir {
    font-size: 13px;
    font-weight: lighter;
  }

  .payNowOneRow {
    width: 32vw;
    display: flex;
    align-items: center;
    margin-top: 10px;
    height: 30px;
  }

  .payNowOneRow input {
    border: none;
    outline: none;
    border-bottom: solid 1px var(--primary-1);
    margin-right: 11px;
    color: var(--primary-4);
    padding: 5px;
    background: transparent;
  }
  .payInput {
    width:60%;
  }
  input::placeholder {
    color: var(--primary-1);
  }
  #app .myParent .dropdownMenu .multiselect__tags,.multiselect__single, .multiselect__element{
    /* background-color: var(--lightColor); */
    font-size: 12px;
    color: var(--primary-0) ;
    border-radius: 5px;
    padding:2px;
  }

  .dropdownMenu{
    margin-left: 10px;
    position: relative;
    /* width: 100%; */
  }
  .dropdownMenu:hover{
    cursor:pointer;
  }
</style>
