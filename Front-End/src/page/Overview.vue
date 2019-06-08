<template>
  <div class='overviewComponent'>
    <SidebarComponent />

    <div class='rightPanelOverview' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Overview'/>

      <div class='bodySection'>
        <div class="overviewLeftSection">
          <div class="overviewLeftUpper">
            <div class="overviewLeftCard">
              <div class="overviewLeftHeader">
                <img src="../assets/balance.png" alt="Balance">
              </div>

              <div class="overviewLeftBody">
                <div class="overviewLeftBodyUpper">
                  <div class="overviewLeftBodyUpperTitle">Balance</div>
                  <div class="overviewLeftBodyUpperValue">Rp 3.000.000</div>
                </div>

                <div class="overviewLeftBodyLower">
                  <img src="../assets/time.png" alt="Time" width='15px'>
                  <div>Last updated 3 hours ago.</div>
                </div>
              </div>
            </div>

            <div class="overviewLeftCard">
              <div class="overviewLeftHeader">
                <img src="../assets/members.png" alt="Members">
              </div>

              <div class="overviewLeftBody">
                <div class="overviewLeftBodyUpper">
                  <div class="overviewLeftBodyUpperTitle">Members</div>
                  <div class="overviewLeftBodyUpperValue">35</div>
                </div>

                <div class="overviewLeftBodyLower">
                  <img src="../assets/time.png" alt="Time" width='15px'>
                  <div>Last updated 3 hours ago.</div>
                </div>
              </div>
            </div>
          </div>

          <div class="overviewLeftLower">
            <div class="overviewLeftLowerHeader">
              Latest Expenses
            </div>

            <div class="overviewLeftLowerBody">
              <table>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Detail</th>
                    <th>Price</th>
                  </tr>
                </thead>

                <tbody>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                  <tr>
                    <td>1 Maret 2019</td>
                    <td>Beli Meja Bulat</td>
                    <td>Rp 350.000</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <div class="overviewRightSection">
          <div class="overviewRightUpper">
            <div class="overviewRightCard">
              <div class="overviewRightCardHeader">
                98%
              </div>

              <div class="overviewRightCardBody">
                From total payment is paid.
              </div>
            </div>

            <div class="overviewRightCard">
              <div class="overviewRightCardHeader">
                30%
              </div>

              <div class="overviewRightCardBody">
                From total cash is used.
              </div>
            </div>
          </div>

          <div class="overviewRightLower">
            <div class="overviewRightLowerHeader">
              Latest Activity
            </div>

            <div class="overviewRightLowerBody">
              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  Mina requested an expense: Beli Meja
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  1 mins ago
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  Nancy has joined the group. Welcome Nancy!
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  3 mins ago
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  XXX has left the group
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  10 mins ago
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  Tzuyu has made a payment
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  yesterday
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  New expense has been approved: Beli Meja Baru
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  3 days ago
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  Expenses has been rejected: Beli Meja Makan
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  1 week ago
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  Expenses has been rejected: Beli Meja DJ
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  1 month ago
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  Expenses has been rejected: Beli Meja Anak-Anak
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  3 months ago
                </div>
              </div>

              <div class="overviewRightLowerBodyContent">
                <div class="overviewRightLowerBodyContentText">
                  Expenses has been rejected: Beli Meja Ping-Pong
                </div>

                <div class="overviewRightLowerBodyContentTime">
                  <div />
                  last year
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import SidebarComponent from '../components/Sidebar';
  import HeaderSection from '../components/HeaderSection';

  export default {
    computed: {
      rightPanelWidth: function() {
        return (document.documentElement.clientWidth - 280);
      }
    }
  }
</script>

<style>
  .overviewComponent { display: flex; }

  .rightPanelOverview {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .overviewLeftSection, .overviewRightSection { width: 50%; }

  .bodySection {
    width: 100%;
    margin-top: 20px;
    display: flex;
  }

  .overviewLeftUpper {
    display: flex;
    justify-content: space-between;
    height: 155px;
  }

  .overviewLeftCard { width: 48%; }

  .overviewLeftHeader {
    width: 80px;
    height: 80px;
    background-color: var(--primary-0);
    box-sizing: border-box;
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
    position: relative;
    z-index: 1;
    margin-left: 10px;
    display: flex;
    align-items: center;
  }

  .overviewLeftHeader img {
    width: 55%;
    margin: auto;
  }

  .overviewLeftBody {
    background-color: var(--lightColor);
    height: fit-content;
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    position: relative;
    top: -65px;
  }

  .overviewLeftBodyUpper {
    text-align: right;
    padding: 30px 12px 13px 15px;
  }

  .overviewLeftBodyUpperTitle {
    font-size: 12px;
    color: var(--primary-0);
  }

  .overviewLeftBodyUpperValue {
    font-weight: 600;
    color: var(--primary-0);
  }

  .overviewLeftBodyLower {
    border-top: solid 1px white;
    color: var(--primary-0);
    font-size: 12px;
    display: flex;
    align-items: center;
    padding: 10px 10px 10px 17px;
  }

  .overviewLeftBodyLower img { margin-right: 6px; }
  .overviewLeftLower { height: 450px; }

  .overviewLeftLowerHeader {
    background-color: var(--primary-0);
    border-radius: 5px;
    color: var(--lightColor);
    font-weight: 600;
    padding: 15px;
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
  }

  .overviewLeftLowerBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    position: relative;
    top: -30px;
    padding-top: 40px;
    height: 405px;
    box-sizing: border-box;
    line-height: 35px;
    text-align: center;
    font-size: 14px;
    color: var(--primary-0);
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }

  .overviewLeftLowerBody table {
    width: 450px;
    border-spacing: 0;
    margin: auto;
  }

  .overviewLeftLowerBody tbody {
    height: 330px;
    overflow-y: auto;
    overflow-x: hidden;
    margin: auto;
    border-top: solid 1px var(--primary-1);
  }

  .overviewLeftLowerBody tbody, .overviewLeftLowerBody thead tr { display: block; }
  .overviewLeftLowerBody tbody td:nth-child(1), .overviewLeftLowerBody thead tr th:nth-child(1) { width: 130px; }
  .overviewLeftLowerBody tbody td:nth-child(2), .overviewLeftLowerBody thead tr th:nth-child(2) { width: 270px; }
  .overviewLeftLowerBody tbody td:nth-child(3), .overviewLeftLowerBody thead tr th:nth-child(3) { width: 130px; }

  .overviewRightUpper {
    display: flex;
    justify-content: space-between;
    padding-left: 40px;
    height: 175px;
  }

  .overviewRightCard {
    width: 47%;
  }

  .overviewRightCardHeader {
    font-size: 40px;
    font-weight: 600;
    color: var(--lightColor);
    background-color: var(--primary-0);
    border-radius: 15px;
    padding: 30px 15px;
    width: 40%;
    position: relative;
    z-index: 1;
    margin-left: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
  }

  .overviewRightCardBody {
    background-color: var(--lightColor);
    color: var(--primary-0);
    font-size: 14px;
    font-weight: 600;
    border-radius: 5px;
    padding: 10px;
    position: relative;
    top: -90px;
    padding-top: 100px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }

  .overviewRightLower {
    margin-left: 40px;
  }

  .overviewRightLowerHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    border-radius: 5px;
    padding: 12px 18px;
    font-weight: 600;
    width: 88%;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .2);
  }

  .overviewRightLowerBody {
    background-color: var(--lightColor);
    border-radius: 7px;
    position: relative;
    top: -30px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    height: 390px;
    box-sizing: border-box;
    margin-bottom: 0;
    overflow: auto;
    padding: 35px 20px 0 20px;
  }

  .overviewRightLowerBodyContent {
    display: flex;
    justify-content: space-between;
    padding: 13px 0;
    color: var(--primary-0);
    border-bottom: solid 1px var(--primary-1);
  }

  .overviewRightLowerBodyContentText {
    width: 75%;
    font-size: 14px;
    line-height: 22px;
  }

  .overviewRightLowerBodyContentTime {
    width: 20%;
    font-size: 11px;
  }

  .overviewRightLowerBodyContentText, .overviewRightLowerBodyContentTime {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
