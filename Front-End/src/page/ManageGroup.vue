<template>
  <div class='manageGroupComponent'>
    <SidebarComponent />

    <div class='rightPanel' :style="{ width: rightPanelWidth + 'px' }">
      <HeaderSection headerTitle='Manage Group'/>

      <div class="managementTable">
        <div class="managementTableHead">
          <div class="managementTableHeadTitle">
            All Group
          </div>

          <input class='managementTableHeadSearch' type="text" placeholder="Search by anything" />

          <select class='managementTableHeadSort' name="sortBy" id="sortBy">
            <option style='display: none' value="default">Sort By</option>
            <option value="default">Name</option>
            <option value="default">Balance</option>
            <option value="default">Total Members</option>
            <option value="default">Created Date</option>
          </select>

          <div class="managementTableHeadAddNew">
            Add New Group
          </div>
        </div>

        <div class="managementTableBody">
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Balance</th>
                <th>Total Members</th>
                <th>Created Date</th>
              </tr>
            </thead>

            <tbody>
              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

                            <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
              </tr>

              <tr>
                <td>Nadem Ecno</td>
                <td>Rp 3.000.000</td>
                <td>31</td>
                <td>15 March 2019</td>
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
  .manageGroupComponent {
    display: flex;
  }

  .rightPanel {
    padding: 20px 20px 0 30px;
    box-sizing: border-box;
  }

  .managementTable {
    margin-top: 20px;
    height: 80vh;
  }

  .managementTableHead {
    background-color: var(--primary-0);
    color: var(--lightColor);
    display: flex;
    justify-content: space-between;
    padding: 15px 25px;
    border-radius: 5px;
    align-items: center;
    width: 90%;
    margin: auto;
    position: relative;
    z-index: 1;
    box-shadow: 2px 2px 4px rgba(0, 0, 0, .3);
  }

  .managementTableHeadTitle {
    font-size: 20px;
    font-weight: 600;
  }

  .managementTableHeadSearch {
    outline: none;
    padding: 8px 10px;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
  }

  .managementTableHeadSearch::placeholder {
    color: var(--primary-1)
  }

  .managementTableHeadSort {
    outline: none;
    border: none;
    color: var(--primary-0);
    border-radius: 4px;
    width: 100px;
    height: 29px;
  }

  .managementTableBody {
    background-color: var(--lightColor);
    color: var(--primary-0);
    position: relative;
    top: -45px;
    border-radius: 5px;
    height: 70vh;
    padding: 60px 20px 20px 20px;
  }

  .managementTableHeadAddNew {
    background-color: var(--lightColor);
    color: var(--primary-0);
    padding: 10px;
    font-weight: 600;
    border-radius: 5px;
  }

  .managementTableHeadAddNew:hover {
    background-color: var(--primary-3);
    color: var(--lightColor);
    cursor: pointer;
  }

  .managementTableHeadAddNew:active {
    background-color: var(--primary-4);
  }

  .manageGroupComponent .managementTableBody table {
    width: 100%;
    text-align: center;
  }

  .managementTableBody tbody {
    height: 68vh;
    overflow-y: auto;
    overflow-x: hidden;
    box-sizing: border-box;
    border-top: solid 1px var(--primary-1);
    line-height: 42px;
  }

  .manageGroupComponent .managementTableBody thead tr, .managementTableBody tbody { display: block; box-sizing: border-box; }
  .manageGroupComponent .managementTableBody tbody td:nth-child(1), .manageGroupComponent .managementTableBody thead tr th:nth-child(1) { width: 25vw; text-align: left;}
  .manageGroupComponent .managementTableBody tbody td:nth-child(2), .manageGroupComponent .managementTableBody thead tr th:nth-child(2) { width: 10vw; }
  .manageGroupComponent .managementTableBody tbody td:nth-child(3), .manageGroupComponent .managementTableBody thead tr th:nth-child(3) { width: 25vw;}
  .manageGroupComponent .managementTableBody tbody td:nth-child(4), .manageGroupComponent .managementTableBody thead tr th:nth-child(4) { width: 10vw;}
</style>
