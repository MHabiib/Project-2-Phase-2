<template>
  <div class='paymentContainer'>
    <SidebarComponent />

    <div class="rightPanel" :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Payment' />

      <div class="bodySection">
        <div class="paymentTable">
          <div class="paymentTableHeader">
            <div class="paymentTableHeaderUpper">
              <div class='cursorPointer'>prev ---</div>
              <div>2019</div>
              <div class='cursorPointer'>--- next</div>
            </div>

            <div class="paymentTableHeaderLower">
              Periode
            </div>
          </div>

          <div class="paymentTableBody">
            <div class="paymentTableBodyPart theMostLeft">
              <div class='cursorPointer'>January (10/10)</div>
              <div class='cursorPointer'>February (11/11)</div>
              <div class='cursorPointer'>March (12/12)</div>
              <div class='cursorPointer'>April (13/13)</div>
              <div class='cursorPointer'>May (14/14)</div>
              <div class='cursorPointer'>June (15/15)</div>
              <div class='cursorPointer'>July (16/16)</div>
              <div class='cursorPointer'>August (17/17)</div>
              <div class='cursorPointer'>September (18/18)</div>
              <div class='cursorPointer'>October (19/19)</div>
              <div class='cursorPointer'>November (20/20)</div>
              <div class='cursorPointer'>December (21/21)</div>
            </div>
            <div class="paymentTableBodyPart">
              <div>Nama Yang Belum Bayar</div>
              <div>Nama Yang Belum Bayar</div>
              <div>Nama Yang Belum Bayar</div>
              <div>Nama Yang Belum Bayar</div>
              <div>Nama Yang Belum Bayar</div>
              <div>Nama Yang Belum Bayar</div>
            </div>
            <div class="paymentTableBodyPart theMostRight">
              <div>Nama Sendiri</div>
              <div>Nama Yang Udah Bayar</div>
              <div>Nama Yang Udah Bayar</div>
              <div>Nama Yang Udah Bayar</div>
              <div>Nama Yang Udah Bayar</div>
              <div>Nama Yang Udah Bayar</div>
              <div>Nama Yang Udah Bayar</div>
              <div>Nama Yang Udah Bayar</div>
              <div>Nama Yang Udah Bayar</div>
            </div>
          </div>
        </div>

        <div class="statsArea">
          <div class="statsItem">
            <div class="statsHeader">Cash Right Now</div>
            <div class="statsBody">Rp 3.300.000</div>
          </div>

          <div class="statsItem">
            <div class="statsHeader">Total If Everybody Has Paid</div>
            <div class="statsBody">Rp 5.200.000</div>
          </div>

          <div class="statsItem">
            <div class="statsHeader">Total Unpaid</div>
            <div class="statsBody">Rp 1.900.000</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  document.title = 'Payment | Team Cash Flow Management'

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    }
  }
</script>

<style>
  .paymentContainer {
    display: flex;
  }

  .rightPanel {
    padding: 20px 30px 0 30px;
    box-sizing: border-box;
  }

  .bodySection {
    display: flex;
    padding-top: 10px;
    justify-content: space-between;
  }

  .paymentTable {
    width: 65%;
  }

  .paymentTableHeader {
    width: 95%;
    margin: auto;
    background-color: var(--primary-0);
    color: var(--lightColor);
    border-radius: 10px;
    padding: 10px 17px;
    box-sizing: border-box;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    position: relative;
    z-index: 1;
  }

  .paymentTableHeaderUpper {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
  }

  .paymentTableHeaderLower {
    text-align: center;
    font-size: 20px;
  }

  .paymentTableBody {
    width: 100%;
    height: 92%;
    display: flex;
  }

  .paymentTableBodyPart {
    width: 33.33%;
    line-height: 41px;
    text-align: center;
    font-size: 14px;
    position: relative;
    top: -45px;
    padding-top: 55px;
    height: 92%;
    box-shadow: 1px 3px 2px rgba(0, 0, 0, .2);
  }

  .theMostLeft {
    background-color: var(--lightColor);
    border-radius: 10px 0 0 10px;
  }

  .theMostRight {
    border-radius: 0 10px 10px 0;
  }

  .statsArea {
    width: 32%;
  }

  .statsHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    padding: 15px;
    border-radius: 7px;
    position: relative;
    z-index: 1;
    margin-right: 20px;
    height: 75px;
    font-size: 14px;
  }

  .statsBody {
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    background-color: white;
    padding: 20px;
    font-size: 25px;
    font-weight: 600;
    color: var(--primary-0);
    border-radius: 10px;
    position: relative;
    z-index: 2;
    top: -60px;
    margin-left: 20px;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: space-evenly;
  }
</style>
