<template>
  <div class='fixedPosition'>
    <div class='payNowWindow'>
      <div class='payNowWindowSize'>
        <div class="payNowHeader">
          <div>
            <div class='textPayNow'>Pay Now</div>
            <div class='pembayaranTerakhir'>Pembayaran terakhir Anda: {{data.pembayaranTerakhir}}</div>
          </div>

          <div class='buttonGroup'>
            <div class="closeButton" @click="closePayNowWindow">Close</div>
            <div class="okButton">OK</div>
          </div>
        </div>

        <div class="payNowBody">
          <div class="payNowOneRow">
            Rek Tujuan Transfer: 2420115451{{data.nomor}} a.n. Robin Robin{{data.penerima}}
          </div>

          <div class="payNowOneRow">
            <div>
              <input type="number" name="periode" id="periode" placeholder="Jumlah Periode" v-model='periode'/>
            </div>

            <div>
              Total harus dibayar: Rp {{totalTagihan | thousandSeparators}}
            </div>
          </div>

          <div class="payNowOneRow">
            <input type="text" name="rekeningPengirim" id="rekeningPengirim" placeholder="No. Rekening Anda" @keypress='checkChar'/>
            <input style="flex: 1" type="text" name="namaPengirim" id="namaPengirim" placeholder="Rekening Atas Nama"/>
          </div>

          <div class="payNowOneRow">
            <input type="date" name="tanggalTransfer" id="tanggalTransfer" placeholder="dd/mm/yyyy"/>
            <input type="file" name="buktiTransfer" id="buktiTransfer"/>
          </div>

          <div class="payNowOneRow">
            <input @click="setUntukMemberLain" type="checkbox" name="untukMemberLain" id="untukMemberLain"/>
            <div style="margin-right: 10px;">Bayar untuk member lain?</div>
            <input style="flex: 1" type="text" name="emailMemberLain" id="emailMemberLain" placeholder="Email Member Lain" v-if="untukMemberLain" v-model="emailMemberLain"/>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    data: function() {
      return {
        biaya: '',
        namaPengeluaran: '',
        deskripsiPengeluaran: '',
        data: {},
        periode: '',
        untukMemberLain: false,
        emailMemberLain: ''
      }
    },
    methods: {
      closePayNowWindow() {
        this.$emit('closePayNowWindow')
      },
      checkChar(e) {
        if(e.keyCode >= 48 && e.keyCode <= 57 || e.keyCode === 8) {} else {
          e.preventDefault();
        }
      },
      setUntukMemberLain() {
        this.untukMemberLain = !this.untukMemberLain;
        if(this.untukMemberLain === false) {
          this.emailMemberLain = '';
        }
      }
    },
    computed: {
      totalTagihan: function() {
        return (this.periode * 100000)
      }
    },
  }
</script>

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
    font-size: 12px;
    font-weight: lighter;
  }

  .payNowOneRow {
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

  .payNowOneRow input::placeholder {
    color: var(--primary-1);
  }
</style>
