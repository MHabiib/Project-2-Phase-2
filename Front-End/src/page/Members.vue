<template>
  <div class='membersComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Members'/>

      <div class="membersBodySection">
        <div class="membersTableHeader">
          All Members
        </div>

        <div class="membersTableBody">
          <table>
            <thead>
              <tr>
                <th>Join Date</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
              </tr>
            </thead>

            <tbody>
              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>

              <tr>
                <td>17 September 2019</td>
                <td>Bambang Hermansyah</td>
                <td>bambang.hermansyah@gdn-commerce.com</td>
                <td>081234567890</td>
              </tr>
            </tbody>
          </table>
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
  .membersComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .membersBodySection {
    margin-top: 30px;
  }

  .membersTableHeader {
    background-color: var(--primary-0);
    color: var(--lightColor);
    border-radius: 4px;
    padding: 15px;
    width: fit-content;
    margin-left: 15px;
    font-weight: 600;
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .2);
    position: relative;
    z-index: 1;
  }

  .membersTableBody {
    background-color: var(--lightColor);
    border-radius: 10px;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
    padding: 50px 20px 20px 20px;
    position: relative;
    top: -35px;
    color: var(--primary-0);
    text-align: center;
    line-height: 35px;
  }

  .membersTableBody table {
    width: 100%;
  }

  .membersTableBody tbody {
    height: 65vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
  }

  .membersTableBody thead tr, .membersTableBody tbody { display: block; box-sizing: border-box; }
  .membersTableBody tbody td:nth-child(1), .membersTableBody thead tr th:nth-child(1) { width: 17vw; }
  .membersTableBody tbody td:nth-child(2), .membersTableBody thead tr th:nth-child(2) { width: 27vw; }
  .membersTableBody tbody td:nth-child(3), .membersTableBody thead tr th:nth-child(3) { width: 39vw; }
  .membersTableBody tbody td:nth-child(4), .membersTableBody thead tr th:nth-child(4) { width: 17vw; }
</style>
